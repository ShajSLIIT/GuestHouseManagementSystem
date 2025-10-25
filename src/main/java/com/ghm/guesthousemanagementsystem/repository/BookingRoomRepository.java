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

    List<BookingRoom> findAllByBooking(Booking booking);

    List<BookingRoom> findAllByBooking_BookingId(UUID bookingId);

    List<BookingRoom> findAllByRoom(Room room);

    void deleteAllByBooking(Booking booking);

    List<BookingRoom> findAllByBooking_BookingIdIn(List<UUID> bookingIds);
}
