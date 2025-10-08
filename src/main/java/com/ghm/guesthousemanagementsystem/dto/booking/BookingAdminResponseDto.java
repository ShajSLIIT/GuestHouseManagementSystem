package com.ghm.guesthousemanagementsystem.dto.booking;

import com.ghm.guesthousemanagementsystem.dto.supportingdto.PropertySummaryDto;
import com.ghm.guesthousemanagementsystem.dto.supportingdto.RoomLineItemDto;
import com.ghm.guesthousemanagementsystem.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingAdminResponseDto {
    private UUID bookingId;
    private PropertySummaryDto property;
    private List<RoomLineItemDto> rooms;
    private String referenceId;
    private String guestName;
    private String guestEmail;
    private String guestPhone;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BookingStatus status;
    private int noOfRooms;
    private int noOfGuests;
    private BigDecimal totalPrice;
//    private String customerUniqueId;
    private boolean isPaid;
    private LocalDateTime confirmedAt;
    private LocalDateTime expiredAt;
    private String notes;
    private String token;
}
