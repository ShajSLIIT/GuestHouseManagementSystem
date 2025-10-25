package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.dto.AmenityCreateDto;
import com.ghm.guesthousemanagementsystem.dto.AmenityDto;
import com.ghm.guesthousemanagementsystem.entity.Amenity;
import com.ghm.guesthousemanagementsystem.exception.ResourceNotFoundException;
import com.ghm.guesthousemanagementsystem.mapper.AmenityMapper;
import com.ghm.guesthousemanagementsystem.repository.AmenityRepo;
import com.ghm.guesthousemanagementsystem.service.AmenityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AmenityServiceImpl implements AmenityService {

    private final AmenityRepo amenityRepository;

    @Override
    public AmenityDto createAmenity(AmenityCreateDto amenityCreateDto) {
        Amenity amenity = AmenityMapper.mapToNewAmenity(amenityCreateDto);
        Amenity saved = amenityRepository.save(amenity);
        return AmenityMapper.mapToAmenityDto(saved);
    }

    @Override
    public List<AmenityDto> getAllAmenities(){
        return amenityRepository.findAll().stream()
                .map(AmenityMapper::mapToAmenityDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAmenity(UUID id) {
        if(!amenityRepository.existsById(id)){
            throw new ResourceNotFoundException("Property not found with id " + id);
        }
        amenityRepository.deleteById(id);
    }
}
