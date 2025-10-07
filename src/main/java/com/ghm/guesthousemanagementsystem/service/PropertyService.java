package com.ghm.guesthousemanagementsystem.service;

public interface PropertyService {
    PropertyDto createNewProperty(PropertyDto propertyDto);
    PropertyDto getPropertyDetails(PropertyDto propertyDto);
}
