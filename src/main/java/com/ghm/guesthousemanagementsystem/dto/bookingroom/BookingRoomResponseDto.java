package com.ghm.guesthousemanagementsystem.dto.bookingroom;

import com.ghm.guesthousemanagementsystem.entity.Booking;
//import com.ghm.guesthousemanagementsystem.entity.Room;

import java.time.LocalDate;
import java.util.UUID;

public class BookingRoomResponseDto {

    private UUID id;

    private Booking bookingId;

//    private Room roomId;

    private LocalDate startDate;

    private LocalDate endDate;

}
