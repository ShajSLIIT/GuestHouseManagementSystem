package com.ghm.guesthousemanagementsystem.mapper;

import com.ghm.guesthousemanagementsystem.dto.bookingstatushistory.BookingStatusHistoryResponseDto;
import com.ghm.guesthousemanagementsystem.entity.BookingStatusHistory;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingStatusHistoryMapper {

    public static BookingStatusHistoryResponseDto mapStatusHistoryToStatusHistoryResponseDto(BookingStatusHistory bookingStatusHistory){
        BookingStatusHistoryResponseDto dto = new BookingStatusHistoryResponseDto();

        dto.setBookingId(bookingStatusHistory.getBooking().getBookingId());
        dto.setReferenceId(bookingStatusHistory.getBooking().getReferenceId());
        dto.setToStatus(bookingStatusHistory.getToStatus());
        dto.setFromStatus(bookingStatusHistory.getFromStatus());
        dto.setChangedAt(bookingStatusHistory.getChangedAt());
        dto.setReason(bookingStatusHistory.getReason());

        return dto;
    }

}
