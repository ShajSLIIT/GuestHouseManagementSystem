package com.ghm.guesthousemanagementsystem.service;

import com.ghm.guesthousemanagementsystem.dto.booking.*;
import com.ghm.guesthousemanagementsystem.entity.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingService {

    //Admin
    BookingAdminResponseDto createBookingAsAdmin(BookingCreateRequestDto createDto);
    BookingAdminResponseDto updateBooking(UUID bookingId, BookingUpdateRequestDto updateDto);
    BookingAdminResponseDto confirmBooking(UUID bookingId);
    BookingAdminResponseDto cancelBookingAsAdmin(UUID bookingId, BookingCancelRequestDto cancelDto);
    BookingAdminResponseDto attachRooms(UUID bookingId, BookingAttachRoomsRequestDto attachRoomsDto);
    BookingAdminResponseDto getBookingById(UUID bookingId);
    List<BookingAdminResponseDto> getAllBookingsAsAdmin();
    List<BookingSummaryResponseDto> getAllBookingSummariesAsAdmin();

    //Guest
    BookingGuestResponseDto createBookingAsGuest(BookingCreateRequestDto createDto);
    BookingGuestResponseDto patchBooking(String referenceId, BookingPatchRequestDto patchDto);
    BookingGuestResponseDto amendBooking(String referenceId, BookingAmendRequestDto amendDto);
    BookingGuestResponseDto cancelBookingAsGuest(String referenceId, BookingCancelRequestDto cancelDto);
    BookingGuestResponseDto getBookingByReferenceId(String referenceId);

}
