package com.ghm.guesthousemanagementsystem.dto.booking;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingUpdateRequestDto {

    @NotBlank
    @Size(min = 3, max = 200)
    private String guestName;

    @NotBlank
    @Email
    private String guestEmail;

    @NotBlank
    private String guestPhone;

    @NotNull
    private LocalDate checkInDate;

    @NotNull
    private LocalDate checkOutDate;

    @NotNull
    @Min(1) //Include max as well
    private Integer noOfRooms;

    @NotNull
    @Min(1) //Include max as well
    private Integer noOfGuests;

    private String notes;

    @NotNull
    private List<UUID> roomIds;

    private List<UUID> addonIds;

}
