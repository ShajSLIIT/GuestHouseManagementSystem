package com.ghm.guesthousemanagementsystem.mapper;

import com.ghm.guesthousemanagementsystem.entity.Property;
import com.ghm.guesthousemanagementsystem.entity.Room;

import java.util.ArrayList;
import java.util.List;

public class PropertyMapper {
    public static PropertyDto mapToPropertyDto(Property property) {
        if (property == null) {
            return null;
        } else {
            PropertyDto propertyDto = new PropertyDto();
            propertyDto.setId(property.getId());
            propertyDto.setName(property.getName());
            propertyDto.setDescription(property.getDescription());
            propertyDto.setLocation(property.getLocation());
            propertyDto.setCity(property.getCity());
            propertyDto.setCountry(property.getCountry());
            propertyDto.setIsActive(property.getIsActive());
            propertyDto.setPhoneNumber(property.getPhoneNumber());
            propertyDto.setTotalRooms(property.getTotalRooms());
            propertyDto.setEmail(property.getEmail());
            propertyDto.setCheckInTime(property.getCheckInTime());
            propertyDto.setCheckOutTime(property.getCheckOutTime());
            propertyDto.setCoverImageUrl(property.getCoverImageUrl());

            // Map the list of rooms using a stream
            if (property.getRooms() != null) {
                List<RoomDto> roomDtos = new ArrayList<>();
                for(Room room : property.getRooms()){
                    RoomDto roomDto = RoomMapper.mapToRoomDto(room);
                    roomDtos.add(roomDto);
                }
                propertyDto.setRoomDtos(roomDtos);
            }

            return propertyDto;
        }
    }

    public static Property mapToProperty(PropertyDto propertyDto) {
        if (propertyDto == null) {
            return null;
        } else {
            Property property = new Property();
            property.setId(propertyDto.getId());
            property.setName(propertyDto.getName());
            property.setDescription(propertyDto.getDescription());
            property.setLocation(propertyDto.getLocation());
            property.setCity(propertyDto.getCity());
            property.setCountry(propertyDto.getCountry());
            property.setIsActive(propertyDto.getIsActive());
            property.setPhoneNumber(propertyDto.getPhoneNumber());
            property.setTotalRooms(propertyDto.getTotalRooms());
            property.setEmail(propertyDto.getEmail());
            property.setCheckInTime(propertyDto.getCheckInTime());
            property.setCheckOutTime(propertyDto.getCheckOutTime());
            property.setCoverImageUrl(propertyDto.getCoverImageUrl());

            // Map the list of RoomDtos
            if (propertyDto.getRoomDtos() != null) {
                List<Room> rooms = new ArrayList<>();
                for (RoomDto roomDto : propertyDto.getRoomDtos()) {
                    Room room = RoomMapper.mapToRoom(roomDto);
                    rooms.add(room);
                }
                property.setRooms(rooms);
            }

            return property;
        }
    }

}
