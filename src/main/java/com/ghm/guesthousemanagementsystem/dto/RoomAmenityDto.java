package com.ghm.guesthousemanagementsystem.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoomAmenityDto {

    private UUID id;
    private UUID roomId;
    private AmenityDto amenityDto;
    private Boolean isEnabled;
}
