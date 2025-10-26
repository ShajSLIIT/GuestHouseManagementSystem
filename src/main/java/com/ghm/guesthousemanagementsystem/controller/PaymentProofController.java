// PaymentProofController.java
package com.ghm.guesthousemanagementsystem.controller;

import com.ghm.guesthousemanagementsystem.dto.paymentproof.PaymentProofResponseDto;
import com.ghm.guesthousemanagementsystem.service.PaymentProofService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/payment-proofs")
@RequiredArgsConstructor
public class PaymentProofController {

    private final PaymentProofService paymentProofService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PaymentProofResponseDto> uploadPaymentProof(
            @RequestParam String referenceId,
            @RequestParam MultipartFile file) {

        PaymentProofResponseDto response = paymentProofService.uploadPaymentProof(referenceId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/{referenceId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PaymentProofResponseDto> updatePaymentProof(
            @PathVariable String referenceId,
            @RequestParam MultipartFile file) {

        PaymentProofResponseDto response = paymentProofService.updatePaymentProof(referenceId, file);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{referenceId}")
    public ResponseEntity<PaymentProofResponseDto> getPaymentProofByReferenceId(
            @PathVariable String referenceId) {

        PaymentProofResponseDto response = paymentProofService.getPaymentProofByReferenceId(referenceId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/id/{paymentProofId}")
    public ResponseEntity<PaymentProofResponseDto> getPaymentProof(
            @PathVariable UUID paymentProofId) {

        PaymentProofResponseDto response = paymentProofService.getPaymentProof(paymentProofId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{referenceId}/download")
    public void downloadPaymentProofByReferenceId(
            @PathVariable String referenceId,
            HttpServletResponse response) {

        try {
            byte[] fileContent = paymentProofService.downloadPaymentProofByReferenceId(referenceId);
            PaymentProofResponseDto paymentProof = paymentProofService.getPaymentProofByReferenceId(referenceId);

            response.setContentType(paymentProof.getFileType());
            response.setContentLengthLong(paymentProof.getFileSize());
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + paymentProof.getFileName() + "\""); // Changed to getFileName()

            response.getOutputStream().write(fileContent);
            response.getOutputStream().flush();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/id/{paymentProofId}/download")
    public void downloadPaymentProof(
            @PathVariable UUID paymentProofId,
            HttpServletResponse response) {

        try {
            byte[] fileContent = paymentProofService.downloadPaymentProof(paymentProofId);
            PaymentProofResponseDto paymentProof = paymentProofService.getPaymentProof(paymentProofId);

            response.setContentType(paymentProof.getFileType());
            response.setContentLengthLong(paymentProof.getFileSize());
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + paymentProof.getFileName() + "\""); // Changed to getFileName()

            response.getOutputStream().write(fileContent);
            response.getOutputStream().flush();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{referenceId}")
    public ResponseEntity<Void> deletePaymentProofByReferenceId(@PathVariable String referenceId) {
        paymentProofService.deletePaymentProofByReferenceId(referenceId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/id/{paymentProofId}")
    public ResponseEntity<Void> deletePaymentProof(@PathVariable UUID paymentProofId) {
        paymentProofService.deletePaymentProof(paymentProofId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{referenceId}/exists")
    public ResponseEntity<Boolean> hasPaymentProof(@PathVariable String referenceId) {
        boolean exists = paymentProofService.hasPaymentProof(referenceId);
        return ResponseEntity.ok(exists);
    }
}