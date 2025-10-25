package com.ghm.guesthousemanagementsystem.controller;

import com.ghm.guesthousemanagementsystem.dto.RoomCreateDto;
import com.ghm.guesthousemanagementsystem.dto.RoomDetailsDto;
import com.ghm.guesthousemanagementsystem.dto.RoomDto;
import com.ghm.guesthousemanagementsystem.dto.RoomUpdateDto;
import com.ghm.guesthousemanagementsystem.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RequestMapping("/api/property")
@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/{propertyId}/add-new-room")
    public ResponseEntity<RoomDetailsDto> createRoom(@PathVariable UUID propertyId, @RequestBody RoomCreateDto roomCreateDto){
        RoomDetailsDto created = roomService.createRoom(roomCreateDto, propertyId);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{propertyId}/rooms")
    public ResponseEntity<List<RoomDto>> getAllRooms(@PathVariable UUID propertyId){
        List<RoomDto> rooms = roomService.getAllRoomsByPropertyId(propertyId);
        return ResponseEntity.ok(rooms);
    }

//    @GetMapping("/{propertyId}/rooms/{roomId}")
//    public ResponseEntity<List<RoomDto>> getRoomDetailsById(@PathVariable UUID roomId){
//        List<RoomDto> rooms = roomService.getRoomDetailsById(roomId);
//        return ResponseEntity.ok(rooms);
//    }

    @PatchMapping("/{propertyId}/rooms/{roomId}")
    public ResponseEntity<RoomDetailsDto> updateRoom(@PathVariable UUID roomId, @RequestBody RoomUpdateDto roomUpdateDto){
        RoomDetailsDto updatedRoom = roomService.updateRoom(roomId, roomUpdateDto);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{propertyId}/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable UUID roomId){
        roomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{propertyId}/rooms/{roomId}/show-available")
    public ResponseEntity<RoomDetailsDto> showAvailable(@PathVariable UUID roomId, @RequestBody RoomUpdateDto roomUpdateDto){
        RoomDetailsDto updatedRoom = roomService.showRoom(roomId, roomUpdateDto);
//        RoomDetailsDto updatedRoom = roomService.showRoom(roomId, roomUpdateDto);
        return ResponseEntity.ok(updatedRoom);
    }



}
