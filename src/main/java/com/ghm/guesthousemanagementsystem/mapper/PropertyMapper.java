package com.ghm.guesthousemanagementsystem.mapper;

import com.ghm.guesthousemanagementsystem.dto.supportingdto.PropertySummaryDto;
import com.ghm.guesthousemanagementsystem.entity.Property;

public class PropertyMapper {

    public static PropertySummaryDto mapPropertyToPropertySummaryDto(Property  property) {
        PropertySummaryDto summaryDto = new PropertySummaryDto();

        summaryDto.setPropertyId(property.getId());
        summaryDto.setName(property.getName());
        summaryDto.setLocation(property.getLocation());

        return summaryDto;

    }
}
