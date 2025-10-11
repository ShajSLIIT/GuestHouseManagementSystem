package com.ghm.guesthousemanagementsystem.repository;

import com.ghm.guesthousemanagementsystem.entity.BookingStatusHistory;
import com.ghm.guesthousemanagementsystem.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingStatusHistoryRepository extends JpaRepository<BookingStatusHistory, UUID> {

    List<BookingStatusHistory> findAllByBooking_BookingIdOrderByChangedAtAsc(UUID bookingId);

    List<BookingStatusHistory> findAllByOrderByChangedAtAsc();

    List<BookingStatusHistory> findAllByOrderByChangedAtDesc();

    List<BookingStatusHistory> findAllByToStatusOrderByChangedAtAsc(BookingStatus status);
}
