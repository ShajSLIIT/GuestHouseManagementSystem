package com.ghm.guesthousemanagementsystem.service;

import com.ghm.guesthousemanagementsystem.dto.*;
import com.ghm.guesthousemanagementsystem.entity.Property;

import java.util.List;
import java.util.UUID;

public interface RoomService {

    RoomDetailsDto createRoom(RoomCreateDto dto, UUID propertyId);
    RoomDetailsDto getRoomDetailsById(UUID id);
    RoomDetailsDto updateRoomDetail(UUID id, RoomUpdateDto dto);
    RoomDetailsDto updateRoom(UUID id, RoomUpdateDto dto);
    RoomDetailsDto showRoom(UUID id, RoomUpdateDto dto);
    List<RoomDto> getAllRoomsByPropertyId(UUID propertyId);
    void deleteRoom(UUID id, Property property);
    void deleteRoom(UUID id);
}
