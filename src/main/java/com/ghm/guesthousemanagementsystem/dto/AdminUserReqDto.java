package com.ghm.guesthousemanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserReqDto {
    private String name;
    private String email;
    private String password;
}
