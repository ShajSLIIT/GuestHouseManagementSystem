package com.ghm.guesthousemanagementsystem.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropertySummaryDto {
    private UUID id;
    private String description;
    private String name;
    private String location;
    private String city;
    private String country;
    private Boolean isActive;
    private String coverImageUrl;

}

// Used when listing or showing minimal property data
