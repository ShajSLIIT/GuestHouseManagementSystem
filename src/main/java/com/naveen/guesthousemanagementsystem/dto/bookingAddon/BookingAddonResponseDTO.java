package com.naveen.guesthousemanagementsystem.dto.bookingAddon;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookingAddonResponseDTO {
    private UUID id;
    private Long bookingId;
    private UUID addonId;
    private String addonName;
    private String addonDescription;
    private BigDecimal addonPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}