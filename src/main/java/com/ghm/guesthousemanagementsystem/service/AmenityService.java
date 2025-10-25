package com.ghm.guesthousemanagementsystem.service;

import com.ghm.guesthousemanagementsystem.dto.AmenityCreateDto;
import com.ghm.guesthousemanagementsystem.dto.AmenityDto;

import java.util.List;
import java.util.UUID;

public interface AmenityService {
    AmenityDto createAmenity(AmenityCreateDto amenityCreateDto);
    List<AmenityDto> getAllAmenities();
    void deleteAmenity(UUID id);
}
