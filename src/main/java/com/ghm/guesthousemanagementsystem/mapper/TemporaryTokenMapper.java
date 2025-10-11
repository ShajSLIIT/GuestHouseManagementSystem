package com.ghm.guesthousemanagementsystem.mapper;

import com.ghm.guesthousemanagementsystem.dto.temporarytoken.TemporaryTokenResponseDto;
import com.ghm.guesthousemanagementsystem.entity.TemporaryToken;

public class TemporaryTokenMapper {

    public static TemporaryTokenResponseDto mapTemporaryTokenToTemporaryTokenResponseDto(TemporaryToken temporaryToken) {

        TemporaryTokenResponseDto temporaryTokenResponseDto = new TemporaryTokenResponseDto();

        temporaryTokenResponseDto.setId(temporaryToken.getId());
        temporaryTokenResponseDto.setToken(temporaryToken.getToken());
        temporaryTokenResponseDto.setExpiresAt(temporaryToken.getExpiresAt());
        temporaryTokenResponseDto.setActive(temporaryToken.isActive());

        return temporaryTokenResponseDto;
    }

    // For future use

//    public static TemporaryTokenRequestDto mapTemporaryTokenToTemporaryTokenRequestDto(TemporaryToken temporaryToken) {
//
//        TemporaryTokenRequestDto temporaryTokenRequestDto = new TemporaryTokenRequestDto();
//
//        temporaryTokenRequestDto.setBookingId(temporaryToken.getBooking().getBookingId());
//        temporaryTokenRequestDto.setNewExpiresAt(temporaryToken.getExpiresAt());
//
//        return temporaryTokenRequestDto;
//    }
}
