package com.ghm.guesthousemanagementsystem.controller;

import com.ghm.guesthousemanagementsystem.dto.booking.*;
import com.ghm.guesthousemanagementsystem.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/admin/create")
    public ResponseEntity<BookingAdminResponseDto> createBookingAsAdmin(@Valid @RequestBody BookingCreateRequestDto createDto){
        BookingAdminResponseDto booking = bookingService.createBookingAsAdmin(createDto);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<BookingAdminResponseDto> updateBooking(
                    @PathVariable UUID id,
                    @Valid @RequestBody BookingUpdateRequestDto bookingUpdateRequestDto)
    {
        BookingAdminResponseDto booking = bookingService.updateBooking(id, bookingUpdateRequestDto);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/admin/{id}/confirm")
    public ResponseEntity<BookingAdminResponseDto> confirmBooking(@PathVariable UUID id){
        BookingAdminResponseDto booking = bookingService.confirmBooking(id);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/admin/{id}/cancel")
    public ResponseEntity<BookingAdminResponseDto> cancelBookingAsAdmin(
                    @PathVariable UUID id,
                    @Valid @RequestBody BookingCancelRequestDto cancelDto)
    {
        BookingAdminResponseDto booking = bookingService.cancelBookingAsAdmin(id, cancelDto);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/admin/{id}/attach-rooms")
    public ResponseEntity<BookingAdminResponseDto> attachRooms(
                    @PathVariable UUID id,
                    @Valid @RequestBody BookingAttachRoomsRequestDto attachRoomDto){
        BookingAdminResponseDto booking = bookingService.attachRooms(id, attachRoomDto);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<BookingAdminResponseDto> getBookingById(@PathVariable UUID id){
        BookingAdminResponseDto booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<BookingSummaryResponseDto>> getAllBookingsAsAdmin(){
        List<BookingSummaryResponseDto> bookings = bookingService.getAllBookingsAsAdmin();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/admin/summary")
    public ResponseEntity<List<BookingSummaryResponseDto>> getAllBookingSummariesAsAdmin(){
        List<BookingSummaryResponseDto> bookings = bookingService.getAllBookingSummariesAsAdmin();
        return ResponseEntity.ok(bookings);
    }

    @PostMapping("/guest/create")
    public ResponseEntity<BookingGuestResponseDto> createBookingAsGuest(@Valid @RequestBody BookingCreateRequestDto createDto){
        BookingGuestResponseDto booking = bookingService.createBookingAsGuest(createDto);
        return ResponseEntity.ok(booking);
    }

    @PatchMapping("/guest/{id}")
    public ResponseEntity<BookingGuestResponseDto> patchBooking(
                    @PathVariable UUID id,
                    @Valid @RequestBody BookingPatchRequestDto patchDto)
    {
        BookingGuestResponseDto booking = bookingService.patchBooking(id, patchDto);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/guest/{id}/amend")
    public ResponseEntity<BookingGuestResponseDto> amendBooking(
                    @PathVariable UUID id,
                    @Valid @RequestBody BookingAmendRequestDto amendDto)
    {
        BookingGuestResponseDto booking = bookingService.amendBooking(id, amendDto);
        return ResponseEntity.ok(booking);
    }

    @PostMapping("/guest/{id}/cancel")
    public ResponseEntity<BookingGuestResponseDto> cancelBookingAsGuest(
                    @PathVariable UUID id,
                    @Valid @RequestBody BookingCancelRequestDto cancelDto)
    {
        BookingGuestResponseDto booking = bookingService.cancelBookingAsGuest(id, cancelDto);
        return ResponseEntity.ok(booking);
    }

    @GetMapping("/guest/{token}")
    public ResponseEntity<BookingGuestResponseDto> getBookingByToken(@PathVariable String token)
    {
        BookingGuestResponseDto booking = bookingService.getBookingByToken(token);
        return ResponseEntity.ok(booking);
    }
}
