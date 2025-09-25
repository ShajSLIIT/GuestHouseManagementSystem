package com.ghm.guesthousemanagementsystem.dto;
import com.ghm.guesthousemanagementsystem.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDto {
    private UUID id;
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
    private List<RoomDto> roomDtos;


//    private UUID managerId;   // flat reference
//    private Integer roomCount; // convenience field

}
