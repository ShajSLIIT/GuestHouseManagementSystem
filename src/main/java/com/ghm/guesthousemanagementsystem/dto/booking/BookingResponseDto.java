package com.ghm.guesthousemanagementsystem.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    private UUID bookingId;
//    private UUID propertyId;
    private String referenceId;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    private int noOfRooms;
    private int noOfGuests;
    private BigDecimal totalPrice;
    private String customerUniqueId;
    private boolean isPaid;
    private LocalDateTime confirmedAt;
    private LocalDateTime expiredAt;
    private String notes;
    private char token;
}
