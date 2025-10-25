package com.ghm.guesthousemanagementsystem.mapper;

import com.ghm.guesthousemanagementsystem.dto.*;
import com.ghm.guesthousemanagementsystem.dto.RoomLineItemDto;
import com.ghm.guesthousemanagementsystem.entity.Property;
import com.ghm.guesthousemanagementsystem.entity.Room;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class RoomMapper {

    public static Room mapToNewRoom(RoomCreateDto roomDto, Property property){
        Room room = new Room();
        room.setRoomType(roomDto.getRoomType());
        room.setRoomNumber(roomDto.getRoomNumber());
        room.setDescription(roomDto.getDescription());
        room.setPricePerNight(roomDto.getPricePerNight());
        room.setMaxOccupancy(roomDto.getMaxOccupancy());
//        room.setIsAvailable(roomDto.getIsAvailable());
        room.setIsAvailable(false);
        room.setImageUrl(roomDto.getImageUrl());
        room.setProperty(property); // set owning side
        return room;
    }


    public static RoomDto mapToRoomDto(Room room) {
        RoomDto dto = new RoomDto();
        dto.setId(room.getId());
        dto.setRoomType(room.getRoomType());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setDescription(room.getDescription());
        dto.setIsAvailable(room.getIsAvailable());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setPropertyId(room.getProperty().getId());
        return dto;
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
//            room.setMaxOccupancy(roomDto.getMaxOccupancy());
            room.setIsAvailable(roomDto.getIsAvailable());
//            room.setImageUrl(roomDto.getImageUrl());

            if (roomDto.getPropertyId() != null) {
                Property property = new Property();
                property.setId(roomDto.getPropertyId());
                room.setProperty(property);
            }
            return room;
        }
    }
    public static RoomDetailsDto mapToRoomDetailsDto(Room room) {
        RoomDetailsDto dto = new RoomDetailsDto();
        dto.setId(room.getId());
        dto.setRoomType(room.getRoomType());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setDescription(room.getDescription());
        dto.setPricePerNight(room.getPricePerNight());
        dto.setMaxOccupancy(room.getMaxOccupancy());
        dto.setIsAvailable(room.getIsAvailable());
        dto.setPropertyId(room.getProperty().getId());
        dto.setImageUrl(room.getImageUrl());

        dto.setAmenities(RoomAmenityMapper.toDtoList(room.getAmenities()));


        return dto;
    }

    public static void updateRoomFromDto(RoomUpdateDto dto, Room room){
        if (dto.getRoomType() != null) {room.setRoomType(dto.getRoomType());}
        if (dto.getRoomNumber() != null) {room.setRoomNumber(dto.getRoomNumber());}
        if (dto.getDescription() != null) {room.setDescription(dto.getDescription());}
        if (dto.getPricePerNight() != null) {room.setPricePerNight(dto.getPricePerNight());}
        if (dto.getMaxOccupancy() != null) {room.setMaxOccupancy(dto.getMaxOccupancy());}
        if (dto.getIsAvailable() != null) {room.setIsAvailable(dto.getIsAvailable());}
        if (dto.getImageUrl() != null) {room.setImageUrl(dto.getImageUrl());}
        // Optionally update property if allowed from DTO
        if (dto.getPropertyId() != null) {
            Property property = new Property();
            property.setId(dto.getPropertyId());
            room.setProperty(property);
        }
    }

    //    -------------------------------------------GR
    public static List<RoomLineItemDto> mapRoomToRoomLineDto(List<Room> rooms, LocalDate checkInDate, LocalDate checkOutDate) {

        List<RoomLineItemDto> roomLineItemDtos = new ArrayList<>();
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        for (Room room : rooms){
            RoomLineItemDto roomLineItemDto = new RoomLineItemDto();

            roomLineItemDto.setRoomId(room.getId());
            roomLineItemDto.setRoomNumber(room.getRoomNumber());
            roomLineItemDto.setNoOfNights(days);
            roomLineItemDto.setPricePerNight(room.getPricePerNight());
            roomLineItemDto.setLineTotal(room.getPricePerNight().multiply(BigDecimal.valueOf(days)));

            roomLineItemDtos.add(roomLineItemDto);
        }
        return roomLineItemDtos;
    }

    public static void showRoomFromDto(RoomUpdateDto dto, Room room){
        room.showRoom();
//        if (dto.getRoomType() != null) {room.setRoomType(dto.getRoomType());}
//        if (dto.getRoomNumber() != null) {room.setRoomNumber(dto.getRoomNumber());}
//        if (dto.getDescription() != null) {room.setDescription(dto.getDescription());}
//        if (dto.getPricePerNight() != null) {room.setPricePerNight(dto.getPricePerNight());}
//        if (dto.getMaxOccupancy() != null) {room.setMaxOccupancy(dto.getMaxOccupancy());}
//        if (dto.getIsAvailable() != null) {room.setIsAvailable(dto.getIsAvailable());}
//        if (dto.getImageUrl() != null) {room.setImageUrl(dto.getImageUrl());}
//        // Optionally update property if allowed from DTO
//        if (dto.getPropertyId() != null) {
//            Property property = new Property();
//            property.setId(dto.getPropertyId());
//            room.setProperty(property);
//        }
    }

}
