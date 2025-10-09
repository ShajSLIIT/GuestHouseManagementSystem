package com.naveen.guesthousemanagementsystem.service;

import com.naveen.guesthousemanagementsystem.dto.bookingAddon.BookingAddonRequestDTO;
import com.naveen.guesthousemanagementsystem.dto.bookingAddon.BookingAddonResponseDTO;
import com.naveen.guesthousemanagementsystem.entity.Addon;
import com.naveen.guesthousemanagementsystem.entity.BookingAddon;
import com.naveen.guesthousemanagementsystem.mapper.BookingAddonMapper;
import com.naveen.guesthousemanagementsystem.repository.AddonRepository;
import com.naveen.guesthousemanagementsystem.repository.BookingAddonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingAddonService {

    private final BookingAddonRepository bookingAddonRepository;
    private final AddonRepository addonRepository;
    private final BookingAddonMapper bookingAddonMapper;

    /** Get all addons for specific booking */
    @Transactional(readOnly = true)
    public List<BookingAddonResponseDTO> getAddonsByBooking(Long bookingId) {
        return bookingAddonRepository.findByBookingId(bookingId).stream()
                .map(bookingAddonMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /** Add addon to booking */
    public BookingAddonResponseDTO addAddonToBooking(BookingAddonRequestDTO request) {
        Addon addon = addonRepository.findById(request.getAddonId())
                .orElseThrow(() -> new RuntimeException("Addon not found with id: " + request.getAddonId()));

        if (!addon.getActive()) {
            throw new RuntimeException("Addon is not active");
        }

        if (bookingAddonRepository.existsByBookingIdAndAddonId(request.getBookingId(), request.getAddonId())) {
            throw new RuntimeException("Addon already added to this booking");
        }

        BookingAddon bookingAddon = bookingAddonMapper.toEntity(request);
        BookingAddon saved = bookingAddonRepository.save(bookingAddon);
        return bookingAddonMapper.toResponseDTO(saved);
    }

    /** Update booking addon quantity */
    public BookingAddonResponseDTO updateBookingAddonQuantity(UUID bookingAddonId, Integer quantity) {
        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        BookingAddon bookingAddon = bookingAddonRepository.findById(bookingAddonId)
                .orElseThrow(() -> new RuntimeException("Booking addon not found with id: " + bookingAddonId));

        bookingAddon.setQuantity(quantity);
        BookingAddon updated = bookingAddonRepository.save(bookingAddon);
        return bookingAddonMapper.toResponseDTO(updated);
    }

    /** Remove addon from booking */
    public void removeAddonFromBooking(Long bookingId, UUID addonId) {
        if (!bookingAddonRepository.existsByBookingIdAndAddonId(bookingId, addonId)) {
            throw new RuntimeException("Addon not found in this booking");
        }
        bookingAddonRepository.deleteByBookingIdAndAddonId(bookingId, addonId);
    }

    /** Remove all addons from booking */
    public void removeAllAddonsFromBooking(Long bookingId) {
        bookingAddonRepository.deleteByBookingId(bookingId);
    }

    /** Calculate total price of all addons for booking */
    @Transactional(readOnly = true)
    public BigDecimal calculateTotalAddonsPrice(Long bookingId) {
        List<BookingAddon> bookingAddons = bookingAddonRepository.findByBookingId(bookingId);

        return bookingAddons.stream()
                .map(ba -> {
                    if (ba.getAddon() != null) {
                        return ba.getAddon().getPrice().multiply(BigDecimal.valueOf(ba.getQuantity()));
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}