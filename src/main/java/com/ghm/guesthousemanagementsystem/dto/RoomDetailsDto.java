package com.ghm.guesthousemanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailsDto {
    private UUID id;
    private String roomType;
    private String roomNumber;
    private String description;
    private BigDecimal pricePerNight;
    private Integer maxOccupancy;
    private Boolean isAvailable;
    private UUID propertyId;  // flat reference to owning property
    private String imageUrl;
    private List<String> photoUrls;  // all photos for this room
    private List<RoomAmenityDto> amenities;  // nested amenities for this room
}
