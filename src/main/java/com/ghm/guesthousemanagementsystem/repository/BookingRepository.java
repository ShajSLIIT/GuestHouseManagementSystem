package com.ghm.guesthousemanagementsystem.repository;

import com.ghm.guesthousemanagementsystem.entity.Booking;
import com.ghm.guesthousemanagementsystem.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

//    Optional<Booking> findByReferenceId(String referenceId);

    Optional<Booking> findByBookingId(UUID bookingId);

    Optional<Booking> findByToken(String token);

    @Query("""
        SELECT DISTINCT b
        FROM Booking b
        JOIN BookingRoom br ON br.booking = b
        WHERE br.room.id IN :roomIds
            AND b.status IN :statuses
            AND b.checkInDate < :checkOut
            AND b.checkOutDate > :checkIn
        """)
    List<Booking> findOverlappingBookingsForRoomIds(
            @Param("roomIds") List<UUID> roomIds,
            @Param("statuses") List<BookingStatus> statuses,
            @Param("checkIn") LocalDate checkInDate,
            @Param("checkOut") LocalDate checkOutDate
    );



}
