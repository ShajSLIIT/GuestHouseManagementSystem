package com.ghm.guesthousemanagementsystem.dto.bookingstatushistory;

import com.ghm.guesthousemanagementsystem.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingStatusWithTimestampResponseDto {

    private UUID bookingId;
    private String  referenceId;
    private BookingStatus currentStatus;
    private LocalDateTime changedAt;

}