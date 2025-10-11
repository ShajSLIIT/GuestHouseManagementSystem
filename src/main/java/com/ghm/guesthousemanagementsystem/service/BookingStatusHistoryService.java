package com.ghm.guesthousemanagementsystem.service;

import com.ghm.guesthousemanagementsystem.dto.bookingstatushistory.BookingStatusHistoryResponseDto;
import com.ghm.guesthousemanagementsystem.dto.bookingstatushistory.BookingStatusWithTimestampResponseDto;
import com.ghm.guesthousemanagementsystem.entity.Booking;
import com.ghm.guesthousemanagementsystem.entity.BookingStatusHistory;
import com.ghm.guesthousemanagementsystem.enums.BookingStatus;
import jakarta.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface BookingStatusHistoryService {

    void statusChange(Booking booking, BookingStatus fromStatus, BookingStatus toStatus, @Nullable String reason);

    List<BookingStatusHistoryResponseDto> getStatusHistoryByBookingId(UUID bookingId);

    List<BookingStatusHistoryResponseDto> getAllStatusHistory();

    List<BookingStatusHistoryResponseDto> filterStatusHistory(BookingStatus status);

    List<BookingStatusWithTimestampResponseDto> getBookingByCurrentStatus(BookingStatus targetStatus);
}
