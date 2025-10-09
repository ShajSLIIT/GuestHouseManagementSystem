package com.ghm.guesthousemanagementsystem.controller;

import com.ghm.guesthousemanagementsystem.dto.PropertyCreateDto;
import com.ghm.guesthousemanagementsystem.dto.PropertyDetailsDto;
import com.ghm.guesthousemanagementsystem.dto.PropertyDto;
import com.ghm.guesthousemanagementsystem.dto.PropertyUpdateDto;
import com.ghm.guesthousemanagementsystem.service.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/property/")
public class PropertyController {

    private PropertyService propertyService;

    // Build Add property rest api
    @PostMapping
    public ResponseEntity<PropertyDto> createProperty(@RequestBody PropertyCreateDto propertyDto){
        PropertyDto savedProperty = propertyService.createProperty(propertyDto);
        return new ResponseEntity<>(savedProperty, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<PropertyDto>> getAllProperty(){
        List<PropertyDto> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }

    @GetMapping("{id}")
    public ResponseEntity<PropertyDetailsDto> getPropertyDetailById(@PathVariable("id") UUID propertyId){
        PropertyDetailsDto propertyDetailsDto = propertyService.getPropertyDetailById(propertyId);
        return ResponseEntity.ok(propertyDetailsDto);
    }

    @PatchMapping("{id}")
    public ResponseEntity<PropertyDetailsDto> updatePropertyDetailById(@PathVariable("id") UUID propertyId, @RequestBody PropertyUpdateDto dto){
        PropertyDetailsDto updatedDetail = propertyService.updatePropertyDetail(propertyId, dto);
        return ResponseEntity.ok(updatedDetail);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProperty(@PathVariable("id") UUID propertyId){
        propertyService.deleteProperty(propertyId);
        return ResponseEntity.noContent().build();
    }

}
