package com.ghm.guesthousemanagementsystem.controller;

import com.ghm.guesthousemanagementsystem.dto.AmenityCreateDto;
import com.ghm.guesthousemanagementsystem.dto.AmenityDto;
import com.ghm.guesthousemanagementsystem.service.AmenityService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/amenities")
public class AmenityController {

    private final AmenityService amenityService;

    @PostMapping
    public ResponseEntity<AmenityDto> createAmenity(@RequestBody AmenityCreateDto dto){
        AmenityDto createdAmenity = amenityService.createAmenity(dto);
        return new ResponseEntity<>(createdAmenity, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<AmenityDto>> getAllAmenities() {
        return ResponseEntity.ok(amenityService.getAllAmenities());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAmenity(@PathVariable UUID id) {
        amenityService.deleteAmenity(id);
        return ResponseEntity.noContent().build();
    }
}
