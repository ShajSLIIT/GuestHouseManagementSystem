package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.entity.Room;
import com.ghm.guesthousemanagementsystem.mapper.RoomMapper;
import com.ghm.guesthousemanagementsystem.repository.RoomRepo;
import com.ghm.guesthousemanagementsystem.service.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepo roomRepo;

    @Override
    public RoomDto createRoomDto(RoomDto roomDto) {
        Room room = RoomMapper.mapToRoom(roomDto);
        Room savedRoom = roomRepo.save(room);
        return RoomMapper.mapToRoomDto(savedRoom);
    }

}
