package com.ghm.guesthousemanagementsystem.dto.supportingdto;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertySummaryDto {

    private UUID propertyId;
    private String name;
    private String location;
}
