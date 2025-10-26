package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.dto.bookingroom.DateRangeDto;
import com.ghm.guesthousemanagementsystem.entity.Booking;
import com.ghm.guesthousemanagementsystem.entity.BookingRoom;
import com.ghm.guesthousemanagementsystem.entity.Room;
import com.ghm.guesthousemanagementsystem.enums.BookingStatus;
import com.ghm.guesthousemanagementsystem.exception.RoomUnavailableException;
import com.ghm.guesthousemanagementsystem.repository.BookingRepository;
import com.ghm.guesthousemanagementsystem.repository.BookingRoomRepository;
import com.ghm.guesthousemanagementsystem.service.BookingRoomService;
import jakarta.annotation.Nullable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingRoomServiceImpl implements BookingRoomService {

    private final BookingRepository bookingRepository;
    private final BookingRoomRepository bookingRoomRepository;

    @Override
    public void validateRoomAvailability(List<UUID> roomIds, LocalDate checkInDate, LocalDate checkOutDate, @Nullable UUID excludeBookingId){

        //Fetch overlapping bookings for given dates
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookingsForRoomIds(
                roomIds,
                List.of(BookingStatus.pending, BookingStatus.confirmed),
                checkInDate,
                checkOutDate,
                excludeBookingId);

        //Validating no overlapping books are selected
        if(!overlappingBookings.isEmpty()) {
            throw new RoomUnavailableException("Some rooms are not available for the selected dates.");
        }
    }

    @Override
    public Map<UUID, List<DateRangeDto>> getBookedDateRangesByProperty(UUID propertyId) {

        //Fetch date ranges by using property id
        List<Object[]> results = bookingRepository.findBookedDateRangesByProperty(
                propertyId,
                List.of(BookingStatus.pending, BookingStatus.confirmed)
        );

        //Create a new hash map to store these details
        Map<UUID, List<DateRangeDto>> map = new HashMap<>();

        //Map date ranges and room together
        for (Object[] row : results) {
            UUID roomId = (UUID) row[0];
            LocalDate checkIn = (LocalDate) row[1];
            LocalDate checkOut = (LocalDate) row[2];

            map.computeIfAbsent(roomId, id -> new ArrayList<>())
                    .add(new DateRangeDto(checkIn, checkOut));
        }

        return map;
    }

    @Override
    @Transactional
    public void createBookingRooms(Booking booking, List<Room> rooms, LocalDate checkInDate, LocalDate checkOutDate){

        //Create a new field in booking room
        List<BookingRoom> bookingRooms = rooms.stream()
                .map(room -> new BookingRoom(booking, room, checkInDate, checkOutDate))
                .toList();

        bookingRoomRepository.saveAll(bookingRooms);
    }

    @Override
    @Transactional
    public void updateBookingRooms(Booking booking, List<Room> rooms, LocalDate checkInDate, LocalDate checkOutDate) {

        //Delete old fields in booking room
        bookingRoomRepository.deleteAllByBooking(booking);

        //Create a new field in booking room
        List<BookingRoom> bookingRooms = rooms.stream()
                .map(room -> new BookingRoom(booking, room, checkInDate, checkOutDate))
                .toList();

        bookingRoomRepository.saveAll(bookingRooms);
    }

    @Override
    public List<Room> getRoomsByBookingId(UUID bookingId){

        return bookingRoomRepository.findAllByBooking_BookingId(bookingId)
                .stream()
                .map(BookingRoom::getRoom)
                .toList();
    }

    @Override
    public Map<UUID, List<Room>> mapRoomsByBookingIds(List<UUID> bookingIds){

        //Map booked rooms for given bookings
        List<BookingRoom> bookingRooms = bookingRoomRepository.findAllByBooking_BookingIdIn(bookingIds);

        return bookingRooms.stream()
                .collect(Collectors.groupingBy(
                        br->br.getBooking().getBookingId(),
                        Collectors.mapping(BookingRoom::getRoom, Collectors.toList())
                ));
    }
}
