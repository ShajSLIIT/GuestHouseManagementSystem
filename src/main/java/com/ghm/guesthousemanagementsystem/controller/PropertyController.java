package com.ghm.guesthousemanagementsystem.controller;

import com.ghm.guesthousemanagementsystem.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/property/")
public class PropertyController {

    private PropertyService propertyService;

    // Build Add property rest api
    @PostMapping
    public ResponseEntity<PropertyDto> createProperty(PropertyDto propertyDto){
        PropertyDto savedProperty = propertyService.createNewProperty(propertyDto);
        return new ResponseEntity<>(savedProperty, HttpStatus.CREATED);
    }

}
