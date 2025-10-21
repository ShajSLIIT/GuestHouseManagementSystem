package com.naveen.guesthousemanagementsystem.dto.booking;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingAmendRequestDto {

    @NotNull
    private LocalDate newCheckInDate;

    @NotNull
    private LocalDate newCheckOutDate;

//    @NotNull
//    @Min(1)
//    private Integer newNoOfGuests;  //Only if you want totalPrice based on Guest Count as well
}
