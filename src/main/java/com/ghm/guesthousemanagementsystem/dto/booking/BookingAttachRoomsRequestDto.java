package com.ghm.guesthousemanagementsystem.dto.booking;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingAttachRoomsRequestDto {

    @NotEmpty
    private List<UUID> roomIds;
}
