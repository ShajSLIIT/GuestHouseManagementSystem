package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.entity.Booking;
import com.ghm.guesthousemanagementsystem.entity.BookingStatusHistory;
import com.ghm.guesthousemanagementsystem.enums.BookingStatus;
import com.ghm.guesthousemanagementsystem.repository.BookingStatusHistoryRepository;
import com.ghm.guesthousemanagementsystem.service.BookingStatusHistoryService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingStatusHistoryServiceImpl implements BookingStatusHistoryService {

    BookingStatusHistoryRepository bookingStatusHistoryRepository;

    @Override
    public void statusChange(Booking booking, BookingStatus toStatus, @Nullable String reason) {

        BookingStatusHistory history = new BookingStatusHistory();
        history.setBooking(booking);
        history.setFromStatus(booking.getStatus());
        history.setToStatus(toStatus);
        history.setChangedAt(LocalDateTime.now());

        if(reason != null &&  !reason.isBlank()) {
            history.setReason(reason);
        }

        bookingStatusHistoryRepository.save(history);

    }

}
