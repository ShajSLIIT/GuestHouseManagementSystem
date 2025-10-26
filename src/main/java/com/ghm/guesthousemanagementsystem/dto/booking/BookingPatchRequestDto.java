package com.ghm.guesthousemanagementsystem.dto.booking;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingPatchRequestDto {

    @Size(min = 3, max = 200)
    private String guestName;

    @Email
    private String guestEmail;

    private String guestPhone;

    @Min(value = 1, message = "Value must be at least one")
    private Integer noOfGuests;

    private String notes;

}
