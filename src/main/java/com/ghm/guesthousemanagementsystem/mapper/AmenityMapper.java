package com.ghm.guesthousemanagementsystem.mapper;

import com.ghm.guesthousemanagementsystem.dto.AmenityCreateDto;
import com.ghm.guesthousemanagementsystem.dto.AmenityDto;
import com.ghm.guesthousemanagementsystem.entity.Amenity;

public class AmenityMapper {

    public static AmenityDto mapToAmenityDto(Amenity amenity){
        AmenityDto amenityDto = new AmenityDto();
        amenityDto.setId(amenity.getId());
        amenityDto.setName(amenity.getName());
        amenityDto.setDescription(amenity.getDescription());
        amenityDto.setIconUrl(amenity.getIconUrl());
        amenityDto.setIsActive(amenity.getIsActive());

        return amenityDto;
    }

    public static Amenity mapToAmenity(AmenityCreateDto amenityDto){
        Amenity amenity = new Amenity();
        amenity.setName(amenityDto.getName());
        amenity.setDescription(amenityDto.getDescription());
        amenity.setIsActive(amenityDto.getIsActive());
        amenity.setIconUrl(amenityDto.getIconUrl());

        return amenity;
    }

    public static Amenity mapToNewAmenity(AmenityCreateDto dto){
        Amenity amenity = new Amenity();
        amenity.setName(dto.getName());
        amenity.setDescription(dto.getDescription());
        amenity.setIconUrl(dto.getIconUrl());
        amenity.setIsActive(dto.getIsActive());
        return amenity;
    }
}
