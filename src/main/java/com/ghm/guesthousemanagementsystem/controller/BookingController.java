package com.naveen.guesthousemanagementsystem.controller;

import com.ghm.guesthousemanagementsystem.dto.bookingroom.DateRangeDto;
import com.ghm.guesthousemanagementsystem.service.BookingRoomService;
import com.ghm.guesthousemanagementsystem.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingRoomService bookingRoomService;

    @GetMapping("/availability")
    public ResponseEntity<Map<UUID, List<DateRangeDto>>> getBookedDateRangesByProperty(@RequestParam UUID propertyId) {
        Map<UUID, List<DateRangeDto>> bookedDates = bookingRoomService.getBookedDateRangesByProperty(propertyId);
        return ResponseEntity.ok(bookedDates);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<BookingAdminResponseDto> createBookingAsAdmin(@Valid @RequestBody BookingCreateRequestDto createDto) {
        BookingAdminResponseDto booking = bookingService.createBookingAsAdmin(createDto);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/admin/{id}/update")
    public ResponseEntity<BookingAdminResponseDto> updateBooking(
            @PathVariable UUID id,
            @Valid @RequestBody BookingUpdateRequestDto bookingUpdateRequestDto) {
        BookingAdminResponseDto booking = bookingService.updateBooking(id, bookingUpdateRequestDto);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/admin/{id}/confirm")
    public ResponseEntity<BookingAdminResponseDto> confirmBooking(@PathVariable UUID id) {
        BookingAdminResponseDto booking = bookingService.confirmBooking(id);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/admin/{id}/cancel")
    public ResponseEntity<BookingAdminResponseDto> cancelBookingAsAdmin(
            @PathVariable UUID id,
            @Valid @RequestBody BookingCancelRequestDto cancelDto) {
        BookingAdminResponseDto booking = bookingService.cancelBookingAsAdmin(id, cancelDto);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/admin/{id}/attach-rooms")
    public ResponseEntity<BookingAdminResponseDto> attachRooms(
            @PathVariable UUID id,
            @Valid @RequestBody BookingAttachRoomsRequestDto attachRoomDto) {
        BookingAdminResponseDto booking = bookingService.attachRooms(id, attachRoomDto);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<BookingAdminResponseDto> getBookingById(@PathVariable UUID id) {
        BookingAdminResponseDto booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/admin/get-all")
    public ResponseEntity<List<BookingAdminResponseDto>> getAllBookingsAsAdmin() {
        List<BookingAdminResponseDto> bookings = bookingService.getAllBookingsAsAdmin();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/admin/get-all-summary")
    public ResponseEntity<List<BookingSummaryResponseDto>> getAllBookingSummariesAsAdmin() {
        List<BookingSummaryResponseDto> bookings = bookingService.getAllBookingSummariesAsAdmin();
        return ResponseEntity.ok(bookings);
    }

    @PostMapping("/guest/create")
    public ResponseEntity<BookingGuestResponseDto> createBookingAsGuest(@Valid @RequestBody BookingCreateRequestDto createDto) {
        BookingGuestResponseDto booking = bookingService.createBookingAsGuest(createDto);
        return ResponseEntity.ok(booking);
    }

    @PatchMapping("/guest/{referenceId}/patch")
    public ResponseEntity<BookingGuestResponseDto> patchBooking(
            @PathVariable String referenceId,
            @Valid @RequestBody BookingPatchRequestDto patchDto) {
        BookingGuestResponseDto booking = bookingService.patchBooking(referenceId, patchDto);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/guest/{referenceId}/amend")
    public ResponseEntity<BookingGuestResponseDto> amendBooking(
            @PathVariable String referenceId,
            @Valid @RequestBody BookingAmendRequestDto amendDto) {
        BookingGuestResponseDto booking = bookingService.amendBooking(referenceId, amendDto);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/guest/{referenceId}/cancel")
    public ResponseEntity<BookingGuestResponseDto> cancelBookingAsGuest(
            @PathVariable String referenceId,
            @Valid @RequestBody BookingCancelRequestDto cancelDto) {
        BookingGuestResponseDto booking = bookingService.cancelBookingAsGuest(referenceId, cancelDto);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/guest/{referenceId}")
    public ResponseEntity<BookingGuestResponseDto> getBookingByReferenceId(@PathVariable String referenceId) {
        BookingGuestResponseDto booking = bookingService.getBookingByReferenceId(referenceId);
        return ResponseEntity.ok(booking);
    }
}