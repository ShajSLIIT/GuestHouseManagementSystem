package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.dto.bookingstatushistory.BookingStatusHistoryResponseDto;
import com.ghm.guesthousemanagementsystem.dto.bookingstatushistory.BookingStatusWithTimestampResponseDto;
import com.ghm.guesthousemanagementsystem.entity.Booking;
import com.ghm.guesthousemanagementsystem.entity.BookingStatusHistory;
import com.ghm.guesthousemanagementsystem.enums.BookingStatus;
import com.ghm.guesthousemanagementsystem.mapper.BookingStatusHistoryMapper;
import com.ghm.guesthousemanagementsystem.repository.BookingStatusHistoryRepository;
import com.ghm.guesthousemanagementsystem.service.BookingStatusHistoryService;
import jakarta.annotation.Nullable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingStatusHistoryServiceImpl implements BookingStatusHistoryService {

    private final BookingStatusHistoryRepository bookingStatusHistoryRepository;

    @Override
    public void statusChange(Booking booking,
                             @Nullable BookingStatus fromStatus,
                             BookingStatus toStatus,
                             @Nullable String reason) {

        //Create a new field in booking status history
        BookingStatusHistory history = new BookingStatusHistory();

        //Fill fields with data
        history.setBooking(booking);
        history.setFromStatus(fromStatus);
        history.setToStatus(toStatus);
        history.setChangedAt(LocalDateTime.now());

        if(reason != null &&  !reason.isBlank()) {
            history.setReason(reason);
        }

        //Save the field in the base
        bookingStatusHistoryRepository.save(history);
    }

    @Override
    public List<BookingStatusHistoryResponseDto> getStatusHistoryByBookingId(UUID bookingId){

        //Fetch all booking status history for a booking
        List<BookingStatusHistory> entries = bookingStatusHistoryRepository.findAllByBooking_BookingIdOrderByChangedAtAsc(bookingId);

        //Map status history
        return entries.stream()
                .map(entry ->
                        BookingStatusHistoryMapper.mapStatusHistoryToStatusHistoryResponseDto(entry))
                .toList();
    }

    @Override
    public List<BookingStatusHistoryResponseDto> getAllStatusHistory(){

        //Fetch all status history for all active bookings
        List<BookingStatusHistory> entries = bookingStatusHistoryRepository.findAllByOrderByChangedAtAsc();

        //Map status history
        return entries.stream()
                .map(entry->
                        BookingStatusHistoryMapper.mapStatusHistoryToStatusHistoryResponseDto(entry))
                .toList();
    }

    @Override
    public List<BookingStatusHistoryResponseDto> filterStatusHistory(BookingStatus status){

        //Fetch all status history by a given status
        List<BookingStatusHistory> entries = bookingStatusHistoryRepository.findAllByToStatusOrderByChangedAtAsc(status);

        //Map status history
        return entries.stream()
                .map(entry ->
                        BookingStatusHistoryMapper.mapStatusHistoryToStatusHistoryResponseDto(entry))
                .toList();
    }

    @Override
    public List<BookingStatusWithTimestampResponseDto> getBookingByCurrentStatus(BookingStatus targetStatus) {

        //Fetch all status history by booking's current status
        List<BookingStatusHistory> allEntries = bookingStatusHistoryRepository.findAllByOrderByChangedAtDesc();

        //Create a new hash map
        Map<UUID, BookingStatusHistory> latestByBooking = new LinkedHashMap<>();

        //Add each booking to hash map exactly one time
        for(BookingStatusHistory entry : allEntries) {
            UUID bookingId = entry.getBooking().getBookingId();

            if(!latestByBooking.containsKey(bookingId)){
                latestByBooking.put(bookingId, entry);
            }
        }

        return latestByBooking.values().stream()
                .filter(entry -> entry.getToStatus() == targetStatus)
                .map(entry -> new BookingStatusWithTimestampResponseDto(
                        entry.getBooking().getBookingId(),
                        entry.getBooking().getReferenceId(),
                        entry.getToStatus(),
                        entry.getChangedAt()
                ))
                .toList();
    }

}
