package com.ghm.guesthousemanagementsystem.helper;

import com.ghm.guesthousemanagementsystem.entity.Room;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class PricingCalculator {
    public static BigDecimal calculateTotal(List<Room> rooms, LocalDate checkInDate, LocalDate checkOutDate){
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        BigDecimal totalPrice = BigDecimal.ZERO;

        for(Room room : rooms){
            BigDecimal price = room.getPricePerNight().multiply(BigDecimal.valueOf(days));
            totalPrice = totalPrice.add(price);
        }
        return totalPrice;
    }
}
