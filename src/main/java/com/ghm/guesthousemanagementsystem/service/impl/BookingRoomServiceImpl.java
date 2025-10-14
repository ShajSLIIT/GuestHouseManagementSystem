package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.entity.Booking;
import com.ghm.guesthousemanagementsystem.entity.BookingRoom;
import com.ghm.guesthousemanagementsystem.entity.Room;
import com.ghm.guesthousemanagementsystem.enums.BookingStatus;
import com.ghm.guesthousemanagementsystem.exceptions.RoomUnavailableException;
import com.ghm.guesthousemanagementsystem.repository.BookingRepository;
import com.ghm.guesthousemanagementsystem.repository.BookingRoomRepository;
import com.ghm.guesthousemanagementsystem.service.BookingRoomService;
import jakarta.annotation.Nullable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingRoomServiceImpl implements BookingRoomService {

    private final BookingRepository bookingRepository;
    private final BookingRoomRepository bookingRoomRepository;

    @Override
    public void validateRoomAvailability(List<UUID> roomIds, LocalDate checkInDate, LocalDate checkOutDate, @Nullable UUID excludeBookingId){

        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookingsForRoomIds(
                roomIds,
                List.of(BookingStatus.pending, BookingStatus.confirmed),
                checkInDate,
                checkOutDate,
                excludeBookingId);  //Should check for validation of days in between check_in & check_out

        if(!overlappingBookings.isEmpty()) {
            throw new RoomUnavailableException("Some rooms are not available for the selected dates.");
        }
    }

    @Override
    @Transactional
    public void createBookingRooms(Booking booking, List<Room> rooms, LocalDate checkInDate, LocalDate checkOutDate){

        List<BookingRoom> bookingRooms = rooms.stream()
                .map(room -> new BookingRoom(booking, room, checkInDate, checkOutDate))
                .toList();

        bookingRoomRepository.saveAll(bookingRooms);
    }

    @Override
    @Transactional
    public void updateBookingRooms(Booking booking, List<Room> rooms, LocalDate checkInDate, LocalDate checkOutDate) {

        bookingRoomRepository.deleteAllByBooking(booking);

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

        List<BookingRoom> bookingRooms = bookingRoomRepository.findAllByBooking_BookingIdIn(bookingIds);

        return bookingRooms.stream()
                .collect(Collectors.groupingBy(
                    br->br.getBooking().getBookingId(),
                        Collectors.mapping(BookingRoom::getRoom, Collectors.toList())
                ));
    }

}
