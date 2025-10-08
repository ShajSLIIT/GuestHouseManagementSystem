package com.ghm.guesthousemanagementsystem.dto.bookingstatushistory;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public class BookingStatusHistoryRequestDto {

    @NotNull
    private UUID bookingId;

    private String fromStatus;

    private String toStatus;

    private LocalDateTime changedAt;

}
