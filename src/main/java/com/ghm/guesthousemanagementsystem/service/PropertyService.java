package com.ghm.guesthousemanagementsystem.service;

import com.ghm.guesthousemanagementsystem.dto.PropertyDto;

public interface PropertyService {
    PropertyDto createNewProperty(PropertyDto propertyDto);
    PropertyDto getPropertyDetails(PropertyDto propertyDto);
}
