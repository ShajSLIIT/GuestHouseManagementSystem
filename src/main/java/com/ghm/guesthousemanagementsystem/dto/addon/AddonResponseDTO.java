package com.ghm.guesthousemanagementsystem.dto.addon;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AddonResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}