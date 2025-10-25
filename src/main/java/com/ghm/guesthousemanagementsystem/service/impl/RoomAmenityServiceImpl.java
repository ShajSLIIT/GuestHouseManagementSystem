package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.dto.RoomAmenityCreateDto;
import com.ghm.guesthousemanagementsystem.dto.RoomAmenityDto;
import com.ghm.guesthousemanagementsystem.entity.Amenity;
import com.ghm.guesthousemanagementsystem.entity.Room;
import com.ghm.guesthousemanagementsystem.entity.RoomAmenity;
import com.ghm.guesthousemanagementsystem.exception.ResourceNotFoundException;
import com.ghm.guesthousemanagementsystem.mapper.RoomAmenityMapper;
import com.ghm.guesthousemanagementsystem.repository.AmenityRepo;
import com.ghm.guesthousemanagementsystem.repository.RoomAmenityRepo;
import com.ghm.guesthousemanagementsystem.repository.RoomRepository;
import com.ghm.guesthousemanagementsystem.service.RoomAmenityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomAmenityServiceImpl implements RoomAmenityService {

    private final RoomRepository roomRepository;
    private final AmenityRepo amenityRepository;
    private final RoomAmenityRepo roomAmenityRepository;


    @Override
    public RoomAmenityDto assignAmenityToRoom(RoomAmenityCreateDto dto) {
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        Amenity amenity = amenityRepository.findById(dto.getAmenityId())
                .orElseThrow(() -> new ResourceNotFoundException("Amenity not found"));
        RoomAmenity roomAmenity = RoomAmenityMapper.mapToNewRoomAmenity(dto, amenity, room);
        RoomAmenity saved = roomAmenityRepository.save(roomAmenity);
        return RoomAmenityMapper.mapToRoomAmenityDto(saved);
    }

    @Override
    public List<RoomAmenityDto> getAmenitiesByRoomId(UUID roomId) {
        List<RoomAmenity> roomAmenities = roomAmenityRepository.findByRoomId(roomId);
        return roomAmenities.stream()
                .map(RoomAmenityMapper::mapToRoomAmenityDto)
                .toList();
    }

    @Override
    public void removeAmenityFromRoom(UUID roomAmenityId) {
        if(!roomAmenityRepository.existsById(roomAmenityId)){
            throw new ResourceNotFoundException("Room amenity does not exist");
        }
        roomAmenityRepository.deleteById(roomAmenityId);
    }

}
