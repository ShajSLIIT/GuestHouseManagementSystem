package com.ghm.guesthousemanagementsystem.dto.temporarytoken;

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
public class TemporaryTokenResponseDto {

    private UUID id;
    private String token;
    private LocalDate expiresAt;
    private boolean isActive;

}
