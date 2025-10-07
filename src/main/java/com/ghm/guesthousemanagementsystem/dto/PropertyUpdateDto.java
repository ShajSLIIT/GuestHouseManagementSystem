package com.ghm.guesthousemanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyUpdateDto {
    private String name;
    private String description;
    private String location;
    private String city;
    private String country;
    private Boolean isActive;
    private String phoneNumber;
    private Integer totalRooms;
    private String email;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private String coverImageUrl;
}


// Used when modifying an existing property.