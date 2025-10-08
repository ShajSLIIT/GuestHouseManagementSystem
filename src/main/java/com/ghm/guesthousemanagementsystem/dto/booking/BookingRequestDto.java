package com.ghm.guesthousemanagementsystem.dto.booking;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDto {

    @NotNull
    private UUID propertyId;

    @NotBlank
    private String guestName;

    @Email
    private String guestEmail;

    private String guestPhone;

    @NotBlank
    private LocalDate checkInDate;

    @NotBlank
    private LocalDate checkOutDate;

    @Min(1)
    private int noOfRooms;

    @Min(1)
    private int noOfGuests;

    private String notes;
}
