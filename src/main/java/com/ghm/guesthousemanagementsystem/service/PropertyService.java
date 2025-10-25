package com.ghm.guesthousemanagementsystem.service;

import com.ghm.guesthousemanagementsystem.dto.PropertyCreateDto;
import com.ghm.guesthousemanagementsystem.dto.PropertyDetailsDto;
import com.ghm.guesthousemanagementsystem.dto.PropertySummaryDto;
import com.ghm.guesthousemanagementsystem.dto.PropertyUpdateDto;

import java.util.List;
import java.util.UUID;

public interface PropertyService {
    List<PropertySummaryDto> getAllProperties();
    PropertyDetailsDto getPropertyDetailById(UUID id);
    PropertySummaryDto createProperty(PropertyCreateDto propertyDto, String adminEmail);
    PropertySummaryDto updateProperty(UUID id, PropertyUpdateDto dto);
    PropertyDetailsDto updatePropertyDetail(UUID id, PropertyUpdateDto dto);
    void deleteProperty(UUID id);
}
