package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.dto.bookingstatushistory.BookingStatusHistoryResponseDto;
import com.ghm.guesthousemanagementsystem.dto.bookingstatushistory.BookingStatusWithTimestampResponseDto;
import com.ghm.guesthousemanagementsystem.entity.Booking;
import com.ghm.guesthousemanagementsystem.entity.BookingStatusHistory;
import com.ghm.guesthousemanagementsystem.enums.BookingStatus;
import com.ghm.guesthousemanagementsystem.mapper.BookingStatusHistoryMapper;
import com.ghm.guesthousemanagementsystem.service.BookingStatusHistoryService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingStatusHistoryServiceImpl implements BookingStatusHistoryService {

    private final BookingStatusHistoryRepository bookingStatusHistoryRepository;

    @Override
    public void statusChange(Booking booking,
                             @Nullable BookingStatus fromStatus,
                             BookingStatus toStatus,
                             @Nullable String reason) {

        BookingStatusHistory history = new BookingStatusHistory();
        history.setBooking(booking);
        history.setFromStatus(fromStatus);
        history.setToStatus(toStatus);

        if(reason != null &&  !reason.isBlank()) {
            history.setReason(reason);
        }
        bookingStatusHistoryRepository.save(history);
    }

    @Override
    public List<BookingStatusHistoryResponseDto> getStatusHistoryByBookingId(UUID bookingId){

        List<BookingStatusHistory> entries = bookingStatusHistoryRepository.findAllByBooking_BookingIdOrderByChangedAtAsc(bookingId);

        return entries.stream()
                .map(entry ->
                        BookingStatusHistoryMapper.mapStatusHistoryToStatusHistoryResponseDto(entry))
                .toList();
    }

    @Override
    public List<BookingStatusHistoryResponseDto> getAllStatusHistory(){

        List<BookingStatusHistory> entries = bookingStatusHistoryRepository.findAllByOrderByChangedAtAsc();

        return entries.stream()
                .map(entry->
                        BookingStatusHistoryMapper.mapStatusHistoryToStatusHistoryResponseDto(entry))
                .toList();
    }

    @Override
    public List<BookingStatusHistoryResponseDto> filterStatusHistory(BookingStatus status){
        List<BookingStatusHistory> entries = bookingStatusHistoryRepository.findAllByToStatusOrderByChangedAtAsc(status);

        return entries.stream()
                .map(entry ->
                        BookingStatusHistoryMapper.mapStatusHistoryToStatusHistoryResponseDto(entry))
                .toList();
    }

    @Override
    public List<BookingStatusWithTimestampResponseDto> getBookingByCurrentStatus(BookingStatus targetStatus) {

        List<BookingStatusHistory> allEntries = bookingStatusHistoryRepository.findAllByOrderByChangedAtDesc();

        Map<UUID, BookingStatusHistory> latestByBooking = new LinkedHashMap<>();

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
