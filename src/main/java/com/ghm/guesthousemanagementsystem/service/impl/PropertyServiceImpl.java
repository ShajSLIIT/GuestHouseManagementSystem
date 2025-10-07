package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.entity.Property;
import com.ghm.guesthousemanagementsystem.mapper.PropertyMapper;
import com.ghm.guesthousemanagementsystem.repository.PropertyRepo;
import com.ghm.guesthousemanagementsystem.service.PropertyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepo propertyRepo;

    @Override
    public PropertyDto createNewProperty(PropertyDto propertyDto) {
        Property property = PropertyMapper.mapToProperty(propertyDto);
        Property savedProperty = propertyRepo.save(property);
        return PropertyMapper.mapToPropertyDto(savedProperty);
    }

    @Override
    public PropertyDto getPropertyDetails(PropertyDto propertyDto) {
        // get property from its dto
//        Property property = PropertyMapper.mapToProperty(propertyDto);
//        propertyRepo.findById(property.getId());

        return null;
    }
}
