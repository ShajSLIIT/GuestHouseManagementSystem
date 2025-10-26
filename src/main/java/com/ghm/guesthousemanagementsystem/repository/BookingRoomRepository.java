package com.ghm.guesthousemanagementsystem.repository;

import com.ghm.guesthousemanagementsystem.entity.Booking;
import com.ghm.guesthousemanagementsystem.entity.BookingRoom;
import com.ghm.guesthousemanagementsystem.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRoomRepository extends JpaRepository<BookingRoom, UUID> {

    //Get all booked rooms
    List<BookingRoom> findAllByBooking(Booking booking);

    //Get all booked rooms for a booking
    List<BookingRoom> findAllByBooking_BookingId(UUID bookingId);

    //Delete booking room records
    void deleteAllByBooking(Booking booking);

    //Get all booked rooms for given bookings
    List<BookingRoom> findAllByBooking_BookingIdIn(List<UUID> bookingIds);
}
