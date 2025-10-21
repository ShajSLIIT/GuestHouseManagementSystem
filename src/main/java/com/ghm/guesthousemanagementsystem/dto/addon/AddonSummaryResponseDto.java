package com.naveen.guesthousemanagementsystem.dto.addon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddonSummaryResponseDto {

    private String name;
    private String description;
    private BigDecimal price;
}
