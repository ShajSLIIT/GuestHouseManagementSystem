package com.ghm.guesthousemanagementsystem.dto.booking;

import jakarta.validation.constraints.*;
import lombok.*;

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

    @Email
    private String guestEmail;

    private String guestPhone;

    @NotNull
    private LocalDate checkInDate;

    @NotNull
    private LocalDate checkOutDate;

    @Min(1) //Include max as well
    private Integer noOfRooms;

    @Min(1) //Include max as well
    private Integer noOfGuests;

    private String notes;

    private List<UUID> roomIds;

}
