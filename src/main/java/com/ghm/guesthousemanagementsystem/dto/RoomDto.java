package com.ghm.guesthousemanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

    private UUID id;
    private UUID propertyId;
    private String roomType;
    private String roomNumber;
    private String description;
    private BigDecimal pricePerNight;
    private Boolean isAvailable;
    private String coverPageUrl;

}