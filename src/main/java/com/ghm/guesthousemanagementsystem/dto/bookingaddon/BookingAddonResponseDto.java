package com.naveen.guesthousemanagementsystem.dto.bookingaddon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingAddonResponseDto {
    private UUID id;  // Booking-addon relationship ID
    private UUID addonId;  // Reference to the addon
    private String addonName;  // Addon name for display
    private String addonDescription;  // Addon description for customer info
    private BigDecimal addonPrice;  // Individual addon price
}