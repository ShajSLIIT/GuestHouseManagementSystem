package com.naveen.guesthousemanagementsystem.controller;

import com.naveen.guesthousemanagementsystem.dto.bookingAddon.BookingAddonRequestDTO;
import com.naveen.guesthousemanagementsystem.dto.bookingAddon.BookingAddonResponseDTO;
import com.naveen.guesthousemanagementsystem.service.BookingAddonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/booking-addons")
@RequiredArgsConstructor
@CrossOrigin(origins = "${app.cors.allowed-origins:http://localhost:3000}")
public class BookingAddonController {

    private final BookingAddonService bookingAddonService;

    /** Get all addons for specific booking */
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<BookingAddonResponseDTO>> getBookingAddons(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingAddonService.getAddonsByBooking(bookingId));
    }

    /** Add addon to booking */
    @PostMapping
    public ResponseEntity<BookingAddonResponseDTO> addAddonToBooking(
            @Valid @RequestBody BookingAddonRequestDTO request) {
        BookingAddonResponseDTO response = bookingAddonService.addAddonToBooking(request);
        return ResponseEntity.ok(response);
    }

    /** Update booking addon quantity */
    @PutMapping("/{bookingAddonId}/quantity")
    public ResponseEntity<BookingAddonResponseDTO> updateQuantity(
            @PathVariable UUID bookingAddonId,
            @RequestParam Integer quantity) {
        BookingAddonResponseDTO response = bookingAddonService.updateBookingAddonQuantity(bookingAddonId, quantity);
        return ResponseEntity.ok(response);
    }

    /** Remove addon from booking */
    @DeleteMapping("/booking/{bookingId}/addon/{addonId}")
    public ResponseEntity<Void> removeAddonFromBooking(
            @PathVariable Long bookingId,
            @PathVariable UUID addonId) {
        bookingAddonService.removeAddonFromBooking(bookingId, addonId);
        return ResponseEntity.ok().build();
    }

    /** Remove all addons from booking */
    @DeleteMapping("/booking/{bookingId}")
    public ResponseEntity<Void> removeAllAddonsFromBooking(@PathVariable Long bookingId) {
        bookingAddonService.removeAllAddonsFromBooking(bookingId);
        return ResponseEntity.ok().build();
    }

    /** Calculate total addons price for booking */
    @GetMapping("/booking/{bookingId}/total-price")
    public ResponseEntity<BigDecimal> getTotalAddonsPrice(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingAddonService.calculateTotalAddonsPrice(bookingId));
    }
}