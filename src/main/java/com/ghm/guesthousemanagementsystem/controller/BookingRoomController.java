package com.ghm.guesthousemanagementsystem.controller;

import com.ghm.guesthousemanagementsystem.entity.Room;
import com.ghm.guesthousemanagementsystem.service.BookingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/booking-rooms/admin")
@RequiredArgsConstructor
public class BookingRoomController {

    private final BookingRoomService bookingRoomService;

    //Show booked rooms for a booking
    @GetMapping("/{bookingId}/get")
    public ResponseEntity<List<Room>> getRoomsByBookingId(@PathVariable UUID bookingId){
        List<Room> rooms = bookingRoomService.getRoomsByBookingId(bookingId);
        return ResponseEntity.ok(rooms);
    }

    //Map all booked rooms for given bookings
    @GetMapping("/map")
    public ResponseEntity<Map<UUID, List<Room>>> mapRoomsByBookingIds(@RequestParam List<UUID> bookingIds){
        Map<UUID, List<Room>> maps = bookingRoomService.mapRoomsByBookingIds(bookingIds);
        return ResponseEntity.ok(maps);
    }

}
