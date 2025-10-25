package com.ghm.guesthousemanagementsystem.controller;

import com.ghm.guesthousemanagementsystem.dto.RoomAmenityCreateDto;
import com.ghm.guesthousemanagementsystem.dto.RoomAmenityDto;
import com.ghm.guesthousemanagementsystem.service.RoomAmenityService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/room-amenities")
public class RoomAmenityController {

    private final RoomAmenityService roomAmenityService;

    @PostMapping
    public ResponseEntity<RoomAmenityDto> assignAmenityToRoom(@RequestBody RoomAmenityCreateDto dto){
        RoomAmenityDto created = roomAmenityService.assignAmenityToRoom(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<RoomAmenityDto>> getAmenitiesForRoom(@PathVariable UUID roomId){
        List<RoomAmenityDto> list = roomAmenityService.getAmenitiesByRoomId(roomId);
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomAmenity(@PathVariable UUID id){
        roomAmenityService.removeAmenityFromRoom(id);
        return ResponseEntity.noContent().build();
    }
}
