package com.ghm.guesthousemanagementsystem.mapper;

import com.ghm.guesthousemanagementsystem.dto.RoomDto;
import com.ghm.guesthousemanagementsystem.entity.Property;
import com.ghm.guesthousemanagementsystem.entity.Room;

public class RoomMapper {

    public static RoomDto mapToRoomDto(Room room){
        if(room == null){
            return null;
        }
        else{
            RoomDto roomDto = new RoomDto();
            roomDto.setId(room.getId());
            roomDto.setPropertyId(room.getProperty() != null ? room.getProperty().getId() : null);
            roomDto.setRoomType(room.getRoomType());
            roomDto.setRoomNumber(room.getRoomNumber());
            roomDto.setDescription(room.getDescription());
            roomDto.setPricePerNight(room.getPricePerNight());
            roomDto.setMaxOccupancy(room.getMaxOccupancy());
            roomDto.setIsAvailable(room.getIsAvailable());
            roomDto.setImageUrl(room.getImageUrl());

            return roomDto;
        }
    }

    public static Room mapToRoom(RoomDto roomDto){
        if(roomDto == null){
            return null;
        }
        else{
            Room room = new Room();
            room.setId(roomDto.getId());
            room.setRoomType(roomDto.getRoomType());
            room.setRoomNumber(roomDto.getRoomNumber());
            room.setDescription(roomDto.getDescription());
            room.setPricePerNight(roomDto.getPricePerNight());
            room.setMaxOccupancy(roomDto.getMaxOccupancy());
            room.setIsAvailable(roomDto.getIsAvailable());
            room.setImageUrl(roomDto.getImageUrl());

            if (roomDto.getPropertyId() != null) {
                Property property = new Property();
                property.setId(roomDto.getPropertyId());
                room.setProperty(property);
            }
            return room;
        }
    }

}
