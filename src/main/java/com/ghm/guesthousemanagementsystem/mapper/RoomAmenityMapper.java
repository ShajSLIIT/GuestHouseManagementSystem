package com.ghm.guesthousemanagementsystem.mapper;

import com.ghm.guesthousemanagementsystem.dto.AmenityDto;
import com.ghm.guesthousemanagementsystem.dto.RoomAmenityCreateDto;
import com.ghm.guesthousemanagementsystem.dto.RoomAmenityDto;
import com.ghm.guesthousemanagementsystem.entity.Amenity;
import com.ghm.guesthousemanagementsystem.entity.Room;
import com.ghm.guesthousemanagementsystem.entity.RoomAmenity;

import java.util.List;

public class RoomAmenityMapper {

    public static RoomAmenity mapToNewRoomAmenity(RoomAmenityCreateDto roomAmenityCreateDto, Amenity amenity, Room room){
        RoomAmenity roomAmenity = new RoomAmenity();
        roomAmenity.setAmenity(amenity);
        roomAmenity.setRoom(room);
        roomAmenity.setIsEnabled(roomAmenityCreateDto.getIsEnabled());
        return roomAmenity;
    }

    public static RoomAmenityDto mapToRoomAmenityDto(RoomAmenity roomAmenity){
        if (roomAmenity == null || roomAmenity.getAmenity() == null) {
            return null;
        }

        AmenityDto amenityDto = new AmenityDto();
        amenityDto.setId(roomAmenity.getAmenity().getId());
        amenityDto.setName(roomAmenity.getAmenity().getName());

        RoomAmenityDto dto = new RoomAmenityDto();
        dto.setId(roomAmenity.getId());
        dto.setAmenityDto(amenityDto);
        dto.setIsEnabled(true); // or roomAmenity.getIsEnabled() if you support soft toggle

        return dto;
    }

    public static List<RoomAmenityDto> toDtoList(List<RoomAmenity> roomAmenities) {
        return roomAmenities == null ? List.of() :
                roomAmenities.stream()
                        .map(RoomAmenityMapper::mapToRoomAmenityDto)
                        .toList();
    }

}
