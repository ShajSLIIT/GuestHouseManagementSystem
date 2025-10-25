package com.ghm.guesthousemanagementsystem.service;

import com.ghm.guesthousemanagementsystem.dto.RoomAmenityCreateDto;
import com.ghm.guesthousemanagementsystem.dto.RoomAmenityDto;

import java.util.List;
import java.util.UUID;

public interface RoomAmenityService {
    RoomAmenityDto assignAmenityToRoom(RoomAmenityCreateDto dto);
    List<RoomAmenityDto> getAmenitiesByRoomId(UUID roomId);
    void removeAmenityFromRoom(UUID roomAmenityId);
}

