package com.ghm.guesthousemanagementsystem.mapper;

import com.ghm.guesthousemanagementsystem.dto.supportingdto.RoomLineItemDto;
import com.ghm.guesthousemanagementsystem.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class RoomMapper {

    public static List<RoomLineItemDto> mapRoomToRoomLineDto(List<Room> rooms, LocalDate checkInDate, LocalDate checkOutDate) {

        List<RoomLineItemDto> roomLineItemDtos = new ArrayList<>();
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        for (Room room : rooms){
            RoomLineItemDto roomLineItemDto = new RoomLineItemDto();

            roomLineItemDto.setRoomNumber(room.getRoomNumber());
            roomLineItemDto.setNoOfNights(days);
            roomLineItemDto.setPricePerNight(room.getPricePerNight());
            roomLineItemDto.setLineTotal(room.getPricePerNight().multiply(BigDecimal.valueOf(days)));

            roomLineItemDtos.add(roomLineItemDto);
        }
        return roomLineItemDtos;
    }
}
