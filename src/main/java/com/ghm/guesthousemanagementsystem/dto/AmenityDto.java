package com.ghm.guesthousemanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AmenityDto {
    private UUID id;
    private String name;
    private String description;
    private String iconUrl;// no need iconUrl
    private Boolean isActive;
}
