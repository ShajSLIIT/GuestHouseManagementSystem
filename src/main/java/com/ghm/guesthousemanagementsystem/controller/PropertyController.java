package com.ghm.guesthousemanagementsystem.controller;

import com.ghm.guesthousemanagementsystem.dto.PropertyCreateDto;
import com.ghm.guesthousemanagementsystem.dto.PropertyDetailsDto;
import com.ghm.guesthousemanagementsystem.dto.PropertySummaryDto;
import com.ghm.guesthousemanagementsystem.dto.PropertyUpdateDto;
import com.ghm.guesthousemanagementsystem.service.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/property")
public class PropertyController {

    private PropertyService propertyService;

    // Build Add property rest api
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PropertySummaryDto> createProperty(@RequestBody PropertyCreateDto propertyDto, Authentication authentication){
        String adminEmail = authentication.getName(); // Get current admin email
        PropertySummaryDto savedProperty = propertyService.createProperty(propertyDto, adminEmail);
        return new ResponseEntity<>(savedProperty, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PropertySummaryDto>> getAllProperty(){
        List<PropertySummaryDto> properties = propertyService.getAllProperties();
        return ResponseEntity.ok(properties);
    }

    @GetMapping("{id}")
    public ResponseEntity<PropertyDetailsDto> getPropertyDetailById(@PathVariable("id") UUID propertyId){
        PropertyDetailsDto propertyDetailsDto = propertyService.getPropertyDetailById(propertyId);
        return ResponseEntity.ok(propertyDetailsDto);
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PropertyDetailsDto> updatePropertyDetailById(@PathVariable("id") UUID propertyId, @RequestBody PropertyUpdateDto dto){
        PropertyDetailsDto updatedDetail = propertyService.updatePropertyDetail(propertyId, dto);
        return ResponseEntity.ok(updatedDetail);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProperty(@PathVariable("id") UUID propertyId){
        propertyService.deleteProperty(propertyId);
        return ResponseEntity.noContent().build();
    }

}
