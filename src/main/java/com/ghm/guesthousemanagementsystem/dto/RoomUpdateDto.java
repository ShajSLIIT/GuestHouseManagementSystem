package com.ghm.guesthousemanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomUpdateDto {
    private String roomType;
    private String roomNumber;
    private String description;
    private BigDecimal pricePerNight;
    private Integer maxOccupancy;
    private Boolean isAvailable;
    private String imageUrl;
    private UUID propertyId;
}
