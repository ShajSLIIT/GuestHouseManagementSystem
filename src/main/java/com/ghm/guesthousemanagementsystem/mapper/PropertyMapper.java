package com.ghm.guesthousemanagementsystem.mapper;

import com.ghm.guesthousemanagementsystem.dto.PropertyCreateDto;
import com.ghm.guesthousemanagementsystem.dto.PropertyDetailsDto;
import com.ghm.guesthousemanagementsystem.dto.PropertySummaryDto;
import com.ghm.guesthousemanagementsystem.dto.PropertyUpdateDto;
import com.ghm.guesthousemanagementsystem.entity.Property;

import java.util.Collections;
import java.util.stream.Collectors;

public class PropertyMapper {
    public static PropertySummaryDto mapToPropertyDto(Property property) {
        if (property == null) {
            return null;
        } else {
            PropertySummaryDto propertySummaryDto = new PropertySummaryDto();
            propertySummaryDto.setId(property.getId());
            propertySummaryDto.setName(property.getName());
            propertySummaryDto.setDescription(property.getDescription());
            propertySummaryDto.setLocation(property.getLocation());
            propertySummaryDto.setCity(property.getCity());
            propertySummaryDto.setCountry(property.getCountry());
            propertySummaryDto.setIsActive(property.getIsActive());
            propertySummaryDto.setCoverImageUrl(property.getCoverImageUrl());

            return propertySummaryDto;
        }
    }


    public static Property mapToProperty(PropertySummaryDto propertySummaryDto) {
        if (propertySummaryDto == null) {
            return null;
        } else {
            Property property = new Property();
            property.setName(propertySummaryDto.getName());
            property.setLocation(propertySummaryDto.getLocation());
            property.setCity(propertySummaryDto.getCity());
            property.setCountry(propertySummaryDto.getCountry());
            property.setIsActive(propertySummaryDto.getIsActive());
            property.setCoverImageUrl(propertySummaryDto.getCoverImageUrl());

            return property;
        }
    }

    // the reason this works is that this function works with only primitive types
    public static Property mapToNewProperty(PropertyCreateDto dto){
        Property property = new Property();
        property.setName(dto.getName());
        property.setDescription(dto.getDescription());
        property.setLocation(dto.getLocation());
        property.setCity(dto.getCity());
        property.setCountry(dto.getCountry());
        property.setPhoneNumber(dto.getPhoneNumber());
        property.setEmail(dto.getEmail());
        property.setIsActive(dto.getIsActive());
        property.setCheckInTime(dto.getCheckInTime());
        property.setCheckOutTime(dto.getCheckOutTime());
        property.setTotalRooms(dto.getTotalRooms());
        property.setCoverImageUrl(dto.getCoverImageUrl());
        return property;  // No need to set ID
    }

    public static void updatePropertyFromDto(PropertyUpdateDto dto, Property property){
        if (dto.getName() != null) property.setName(dto.getName());
        if (dto.getDescription() != null) property.setDescription(dto.getDescription());
        if (dto.getLocation() != null) property.setLocation(dto.getLocation());
        if (dto.getCity() != null) property.setCity(dto.getCity());
        if (dto.getCountry() != null) property.setCountry(dto.getCountry());
        if (dto.getEmail() != null) property.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) property.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getCheckInTime() != null) property.setCheckInTime(dto.getCheckInTime());
        if (dto.getCheckOutTime() != null) property.setCheckOutTime(dto.getCheckOutTime());
        if (dto.getCoverImageUrl() != null) property.setCoverImageUrl(dto.getCoverImageUrl());
        if (dto.getTotalRooms() != null) property.setTotalRooms(dto.getTotalRooms());
        if (dto.getIsActive() != null) property.setIsActive(dto.getIsActive());
    }


    public static PropertyDetailsDto mapToPropertyDetailsDto(Property p) {
        PropertyDetailsDto detailsDto = new PropertyDetailsDto();

        detailsDto.setId(p.getId());
        detailsDto.setName(p.getName());
        detailsDto.setDescription(p.getDescription());
        detailsDto.setLocation(p.getLocation());
        detailsDto.setCity(p.getCity());
        detailsDto.setCountry(p.getCountry());
        detailsDto.setIsActive(p.getIsActive());
        detailsDto.setPhoneNumber(p.getPhoneNumber());
        detailsDto.setTotalRooms(p.getTotalRooms());
        detailsDto.setEmail(p.getEmail());
        detailsDto.setCheckInTime(p.getCheckInTime());
        detailsDto.setCheckOutTime(p.getCheckOutTime());
        detailsDto.setCoverImageUrl(p.getCoverImageUrl());

        if (p.getRooms() != null) {
            detailsDto.setRooms(
                    p.getRooms().stream()
                            .map(RoomMapper::mapToRoomDto)
                            .collect(Collectors.toList())
            );
        } else {
            detailsDto.setRooms(Collections.emptyList());
        }

        return detailsDto;
    }



//    -----------------------------------------------------------------------------GR

    public static PropertySummaryDto mapPropertyToPropertySummaryDto(Property  property) {
        PropertySummaryDto summaryDto = new PropertySummaryDto();

        summaryDto.setId(property.getId());
        summaryDto.setName(property.getName());
        summaryDto.setLocation(property.getLocation());
        summaryDto.setDescription(property.getDescription());
        summaryDto.setCity(property.getCity());
        summaryDto.setCountry(property.getCountry());
        summaryDto.setIsActive(property.getIsActive());
        summaryDto.setCoverImageUrl(property.getCoverImageUrl());

        return summaryDto;

    }



}
