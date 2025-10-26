package com.ghm.guesthousemanagementsystem.dto.booking;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreateRequestDto {

    @NotNull
    private UUID propertyId;

    @NotBlank
    @Size(min = 3, max = 200)
    private String guestName;

    @Email
    private String guestEmail;

    private String guestPhone;

    @NotNull
    private LocalDate checkInDate;

    @NotNull
    private LocalDate checkOutDate;

    @Min(1)                     // Would be better if max value is added as well...
    private int noOfRooms;

    @Min(1)                     // Would be better if max value is added as well...
    private int noOfGuests;

    private String notes;

    private List<UUID> roomIds;

    private List<UUID> addonIds;
}
