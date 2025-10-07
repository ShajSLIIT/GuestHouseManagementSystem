package com.ghm.guesthousemanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
    private String username;
    private String email;
    private LocalDateTime lastLogin;
}
