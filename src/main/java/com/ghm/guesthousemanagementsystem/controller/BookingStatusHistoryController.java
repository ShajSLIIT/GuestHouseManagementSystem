package com.ghm.guesthousemanagementsystem.controller;

import com.ghm.guesthousemanagementsystem.dto.bookingstatushistory.BookingStatusHistoryResponseDto;
import com.ghm.guesthousemanagementsystem.dto.bookingstatushistory.BookingStatusWithTimestampResponseDto;
import com.ghm.guesthousemanagementsystem.enums.BookingStatus;
import com.ghm.guesthousemanagementsystem.service.BookingStatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/status-history/admin")
@RequiredArgsConstructor
public class BookingStatusHistoryController {

    private final BookingStatusHistoryService bookingStatusHistoryService;

    @GetMapping("/{id}")
    public ResponseEntity<List<BookingStatusHistoryResponseDto>> getStatusHistoryByBookingId(@PathVariable UUID id){
        List<BookingStatusHistoryResponseDto> history = bookingStatusHistoryService.getStatusHistoryByBookingId(id);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/filter-by-status")
    public ResponseEntity<List<BookingStatusHistoryResponseDto>> getStatusHistory(
            @RequestParam(required = false) BookingStatus status){

        if (status == null){
            return ResponseEntity.ok(bookingStatusHistoryService.getAllStatusHistory());
        }
        else{
            return ResponseEntity.ok(bookingStatusHistoryService.filterStatusHistory(status));
        }
    }

    @GetMapping("/current-status")
    public ResponseEntity<List<BookingStatusWithTimestampResponseDto>> getBookingByCurrentStatus(
            @RequestParam BookingStatus status){
        List<BookingStatusWithTimestampResponseDto> history = bookingStatusHistoryService.getBookingByCurrentStatus(status);
        return ResponseEntity.ok(history);
    }
}
