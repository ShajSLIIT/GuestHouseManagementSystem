package com.ghm.guesthousemanagementsystem.dto.supportingdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomLineItemDto {

    private String roomNumber;
    private long noOfNights;
    private BigDecimal pricePerNight;
    private BigDecimal lineTotal;
}
