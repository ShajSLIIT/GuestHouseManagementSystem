// PaymentProofServiceImpl.java
package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.dto.paymentproof.PaymentProofResponseDto;
import com.ghm.guesthousemanagementsystem.dto.paymentproof.PaymentProofUploadRequestDto;
import com.ghm.guesthousemanagementsystem.entity.Booking;
import com.ghm.guesthousemanagementsystem.entity.PaymentProof;
import com.ghm.guesthousemanagementsystem.exeption.ResourceNotFoundException;
import com.ghm.guesthousemanagementsystem.repository.BookingRepository;
import com.ghm.guesthousemanagementsystem.repository.PaymentProofRepository;
import com.ghm.guesthousemanagementsystem.service.PaymentProofService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentProofServiceImpl implements PaymentProofService {

    private static final List<String> ALLOWED_FILE_TYPES = List.of(
            "image/jpeg", "image/jpg", "image/png", "application/pdf",
            "image/webp", "image/gif"
    );
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private final PaymentProofRepository paymentProofRepository;
    private final BookingRepository bookingRepository;
    @Value("${app.upload.dir:${user.home}/uploads/payment_proof}")
    private String uploadDir;
    @Value("${app.max.file.size:5242880}") // 5MB default
    private long maxFileSize;
    @Value("${app.upload.subdirectory.format:booking_id}") // Options: booking_id, reference_id, date
    private String subdirectoryFormat;

    @Override
    @Transactional
    public PaymentProofResponseDto uploadPaymentProof(PaymentProofUploadRequestDto uploadRequest) {
        return uploadPaymentProof(uploadRequest.getReferenceId(), uploadRequest.getFile());
    }


    @Override
    @Transactional
    public PaymentProofResponseDto uploadPaymentProof(String referenceId, MultipartFile file) {
        // Validate file
        validateFile(file);

        // Find booking by reference ID
        Booking booking = bookingRepository.findByReferenceId(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with reference id: " + referenceId));

        UUID bookingId = booking.getBookingId();

        // Check if payment proof already exists for this booking (One-to-One constraint)
        Optional<PaymentProof> existingProof = paymentProofRepository.findByBookingBookingId(bookingId);
        if (existingProof.isPresent()) {
            throw new IllegalStateException("Payment proof already exists for this booking. Please update or delete the existing one.");
        }

        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename with reference ID and timestamp
            String originalFileName = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFileName);
            String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
            String uniqueFileName = "payment_proof_" + referenceId + "_" + timestamp + "." + fileExtension;

            // Create subdirectory based on configuration
            Path subdirectory = getSubdirectoryPath(booking, uploadPath);
            if (!Files.exists(subdirectory)) {
                Files.createDirectories(subdirectory);
            }

            Path filePath = subdirectory.resolve(uniqueFileName);

            // Save file to filesystem
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Create and save PaymentProof entity
            PaymentProof paymentProof = new PaymentProof();
            paymentProof.setBooking(booking);
            paymentProof.setFileUrl(filePath.toString());
            paymentProof.setFileName(uniqueFileName);
            paymentProof.setOriginalFileName(originalFileName);
            paymentProof.setFileSize(file.getSize());
            paymentProof.setFileType(file.getContentType());
            paymentProof.setUploadedAt(LocalDateTime.now());

            PaymentProof savedPaymentProof = paymentProofRepository.save(paymentProof);

            log.info("Payment proof uploaded successfully for booking reference {}: {}", referenceId, uniqueFileName);
            log.info("File stored at: {}", filePath);

            return mapToDto(savedPaymentProof);

        } catch (IOException e) {
            log.error("Failed to upload payment proof for booking reference {}", referenceId, e);
            throw new RuntimeException("Failed to upload payment proof: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PaymentProofResponseDto updatePaymentProof(String referenceId, MultipartFile file) {
        // Validate file
        validateFile(file);

        // Find existing payment proof by reference ID
        PaymentProof existingProof = paymentProofRepository.findByBookingReferenceIdWithBooking(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment proof not found for booking reference id: " + referenceId));

        Booking booking = existingProof.getBooking();

        try {
            // Delete old file
            Path oldFilePath = Paths.get(existingProof.getFileUrl());
            Files.deleteIfExists(oldFilePath);

            // Generate new filename
            String originalFileName = file.getOriginalFilename();
            String fileExtension = getFileExtension(originalFileName);
            String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
            String uniqueFileName = "payment_proof_" + referenceId + "_" + timestamp + "." + fileExtension;

            // Create subdirectory based on configuration
            Path uploadPath = Paths.get(uploadDir);
            Path subdirectory = getSubdirectoryPath(booking, uploadPath);
            Path newFilePath = subdirectory.resolve(uniqueFileName);

            // Save new file
            Files.copy(file.getInputStream(), newFilePath, StandardCopyOption.REPLACE_EXISTING);

            // Update entity
            existingProof.setFileUrl(newFilePath.toString());
            existingProof.setFileName(uniqueFileName);
            existingProof.setOriginalFileName(originalFileName);
            existingProof.setFileSize(file.getSize());
            existingProof.setFileType(file.getContentType());
            existingProof.setUploadedAt(LocalDateTime.now());

            PaymentProof updatedProof = paymentProofRepository.save(existingProof);

            log.info("Payment proof updated successfully for booking reference {}: {}", referenceId, uniqueFileName);
            log.info("File stored at: {}", newFilePath);

            return mapToDto(updatedProof);

        } catch (IOException e) {
            log.error("Failed to update payment proof for booking reference {}", referenceId, e);
            throw new RuntimeException("Failed to update payment proof: " + e.getMessage());
        }
    }

    @Override
    public PaymentProofResponseDto getPaymentProofByReferenceId(String referenceId) {
        PaymentProof paymentProof = paymentProofRepository.findByBookingReferenceIdWithBooking(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment proof not found for booking reference id: " + referenceId));
        return mapToDto(paymentProof);
    }

    @Override
    public PaymentProofResponseDto getPaymentProof(UUID paymentProofId) {
        PaymentProof paymentProof = paymentProofRepository.findById(paymentProofId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment proof not found with id: " + paymentProofId));
        return mapToDtoWithBooking(paymentProof);
    }

    @Override
    @Transactional
    public void deletePaymentProofByReferenceId(String referenceId) {
        PaymentProof paymentProof = paymentProofRepository.findByBookingReferenceIdWithBooking(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment proof not found for booking reference id: " + referenceId));

        deletePaymentProof(paymentProof.getId());
    }

    @Override
    @Transactional
    public void deletePaymentProof(UUID paymentProofId) {
        PaymentProof paymentProof = paymentProofRepository.findById(paymentProofId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment proof not found with id: " + paymentProofId));

        try {
            // Delete file from filesystem
            Path filePath = Paths.get(paymentProof.getFileUrl());
            Files.deleteIfExists(filePath);

            // Delete entity from database
            paymentProofRepository.delete(paymentProof);

            log.info("Payment proof deleted successfully: {}", paymentProofId);
        } catch (IOException e) {
            log.error("Failed to delete payment proof file: {}", paymentProofId, e);
            throw new RuntimeException("Failed to delete payment proof file: " + e.getMessage());
        }
    }

    @Override
    public byte[] downloadPaymentProofByReferenceId(String referenceId) {
        PaymentProof paymentProof = paymentProofRepository.findByBookingReferenceIdWithBooking(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment proof not found for booking reference id: " + referenceId));
        return downloadPaymentProof(paymentProof.getId());
    }

    @Override
    public byte[] downloadPaymentProof(UUID paymentProofId) {
        PaymentProof paymentProof = paymentProofRepository.findById(paymentProofId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment proof not found with id: " + paymentProofId));

        try {
            Path filePath = Paths.get(paymentProof.getFileUrl());
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            log.error("Failed to download payment proof: {}", paymentProofId, e);
            throw new RuntimeException("Failed to download payment proof: " + e.getMessage());
        }
    }

    @Override
    public boolean hasPaymentProof(String referenceId) {
        return paymentProofRepository.existsByBookingReferenceId(referenceId);
    }

    // Helper method to determine subdirectory structure
    private Path getSubdirectoryPath(Booking booking, Path basePath) {
        return switch (subdirectoryFormat.toLowerCase()) {
            case "reference_id" -> basePath.resolve(booking.getReferenceId());
            case "date" -> basePath.resolve(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
            case "customer" -> basePath.resolve(booking.getGuestName().replaceAll("[^a-zA-Z0-9]", "_"));
            case "booking_id" -> basePath.resolve(booking.getBookingId().toString());
            default -> basePath.resolve(booking.getBookingId().toString());
        };
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty or null");
        }

        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size of " + (maxFileSize / 1024 / 1024) + "MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_FILE_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("File type not allowed. Allowed types: JPEG, PNG, PDF, WEBP, GIF");
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            throw new IllegalArgumentException("File must have an extension");
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    private PaymentProofResponseDto mapToDto(PaymentProof paymentProof) {
        Booking booking = paymentProof.getBooking();
        return new PaymentProofResponseDto(
                booking.getReferenceId(),
                booking.getGuestName(),
                booking.getGuestEmail(),
                paymentProof.getFileUrl(),
                paymentProof.getFileName(),
                paymentProof.getFileSize(),
                paymentProof.getFileType(),
                paymentProof.getUploadedAt()
        );
    }

    private PaymentProofResponseDto mapToDtoWithBooking(PaymentProof paymentProof) {
        // Fetch booking details if not already loaded
        Booking booking = paymentProof.getBooking();
        if (booking.getGuestName() == null) {
            // If lazy loading, we need to fetch the booking details
            booking = bookingRepository.findById(booking.getBookingId())
                    .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        }

        return new PaymentProofResponseDto(
                booking.getReferenceId(),
                booking.getGuestName(),
                booking.getGuestEmail(),
                paymentProof.getFileUrl(),
                paymentProof.getFileName(),
                paymentProof.getFileSize(),
                paymentProof.getFileType(),
                paymentProof.getUploadedAt()
        );
    }
}