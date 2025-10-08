package com.ghm.guesthousemanagementsystem.dto.bookingroom;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public class BookingRoomRequestDto {

    @NotNull
    private UUID bookingId;

    @NotNull
    private UUID roomId;

    @NotBlank
    private LocalDate startDate;

    @NotBlank
    private LocalDate endDate;
}
