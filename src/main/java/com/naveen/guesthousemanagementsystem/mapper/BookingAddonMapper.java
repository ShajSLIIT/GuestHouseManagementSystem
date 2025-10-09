package com.naveen.guesthousemanagementsystem.mapper;

import com.naveen.guesthousemanagementsystem.dto.bookingAddon.BookingAddonRequestDTO;
import com.naveen.guesthousemanagementsystem.dto.bookingAddon.BookingAddonResponseDTO;
import com.naveen.guesthousemanagementsystem.entity.BookingAddon;
import org.springframework.stereotype.Component;

@Component
public class BookingAddonMapper {

    /** Convert BookingAddonRequestDTO to BookingAddon entity */
    public BookingAddon toEntity(BookingAddonRequestDTO requestDTO) {
        if (requestDTO == null) return null;

        return BookingAddon.builder()
                .bookingId(requestDTO.getBookingId())
                .addonId(requestDTO.getAddonId())
                .quantity(requestDTO.getQuantity())
                .build();
    }

    /** Convert BookingAddon entity to BookingAddonResponseDTO */
    public BookingAddonResponseDTO toResponseDTO(BookingAddon bookingAddon) {
        if (bookingAddon == null) return null;

        BookingAddonResponseDTO dto = new BookingAddonResponseDTO();
        dto.setId(bookingAddon.getId());
        dto.setBookingId(bookingAddon.getBookingId());
        dto.setAddonId(bookingAddon.getAddonId());
        dto.setQuantity(bookingAddon.getQuantity());
        dto.setCreatedAt(bookingAddon.getCreatedAt());
        dto.setUpdatedAt(bookingAddon.getUpdatedAt());

        if (bookingAddon.getAddon() != null) {
            dto.setAddonName(bookingAddon.getAddon().getName());
            dto.setAddonDescription(bookingAddon.getAddon().getDescription());
            dto.setAddonPrice(bookingAddon.getAddon().getPrice());
            dto.setTotalPrice(bookingAddon.getAddon().getPrice()
                    .multiply(new java.math.BigDecimal(bookingAddon.getQuantity())));
        }

        return dto;
    }
}