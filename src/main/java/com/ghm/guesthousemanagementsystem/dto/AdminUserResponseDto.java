package com.ghm.guesthousemanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Data
public class AdminUserResponseDto {
    private UUID id;
    private String name;
    private String email;
    private LocalDateTime lastLogin;
}
