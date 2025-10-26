package com.ghm.guesthousemanagementsystem.repository;


import com.ghm.guesthousemanagementsystem.entity.Booking;
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

    Optional<Booking> findByReferenceId(String referenceId);

    Optional<Booking> findByToken(String token);

    @Query("""
            SELECT DISTINCT b
            FROM Booking b
            JOIN BookingRoom br ON br.booking = b
            WHERE br.room.id IN :roomIds
                AND b.status IN :statuses
                AND (:excludeBookingId IS NULL OR br.booking.bookingId <> :excludeBookingId)
                AND b.checkInDate < :checkOut
                AND b.checkOutDate > :checkIn
            """)
    List<Booking> findOverlappingBookingsForRoomIds(
            @Param("roomIds") List<UUID> roomIds,
            @Param("statuses") List<BookingStatus> statuses,
            @Param("checkIn") LocalDate checkInDate,
            @Param("checkOut") LocalDate checkOutDate,
            @Param("excludeBookingId") UUID excludeBookingId
    );

    @Query("""
                SELECT br.room.id, b.checkInDate, b.checkOutDate
                FROM BookingRoom br
                JOIN br.booking b
                WHERE br.room.property.id = :propertyId
                  AND b.status IN :statuses
            """)
    List<Object[]> findBookedDateRangesByProperty(
            @Param("propertyId") UUID propertyId,
            @Param("statuses") List<BookingStatus> statuses
    );
}
