package com.ghm.guesthousemanagementsystem.dto.temporarytoken;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TemporaryTokenRequestDto {

    private UUID bookingId;
    private LocalDate newExpiresAt;
}
