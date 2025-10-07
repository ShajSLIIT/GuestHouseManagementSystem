package com.ghm.guesthousemanagementsystem.dto;

import lombok.*;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyDetailsDto {
    private UUID id;
    private String name;
    private String description;
    private String location;
    private String city;
    private String country;
    private Boolean isActive;
    private String phoneNumber;
    private Integer totalRooms;      // your int column, not rooms.size()
    private String email;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private String coverImageUrl;

    private List<RoomDto> rooms;     // nested rooms

}

// Used when you want all the information about a single property, including rooms.