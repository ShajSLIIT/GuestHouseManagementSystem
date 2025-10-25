package com.ghm.guesthousemanagementsystem.service;

import com.ghm.guesthousemanagementsystem.dto.bookingroom.DateRangeDto;
import com.ghm.guesthousemanagementsystem.entity.Booking;
import com.ghm.guesthousemanagementsystem.entity.BookingRoom;
import com.ghm.guesthousemanagementsystem.entity.Room;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BookingRoomService {

    void validateRoomAvailability(List<UUID> roomIds, LocalDate checkInDate, LocalDate checkOutDate, @Nullable UUID excludeBookingId);

    Map<UUID, List<DateRangeDto>> getBookedDateRangesByProperty(UUID propertyId);

    void createBookingRooms(Booking booking, List<Room> rooms, LocalDate checkInDate, LocalDate checkOutDate);

    void updateBookingRooms(Booking booking, List<Room> rooms, LocalDate checkInDate, LocalDate checkOutDate);

    List<Room> getRoomsByBookingId(UUID bookingId);

    Map<UUID, List<Room>> mapRoomsByBookingIds(List<UUID> bookingIds);

}
