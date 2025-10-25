package com.ghm.guesthousemanagementsystem.service;


import com.ghm.guesthousemanagementsystem.dto.temporarytoken.TemporaryTokenResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TemporaryTokenService {

    void createTemporaryToken(Booking booking, String token, LocalDate expiresAt);

    TemporaryToken getTokenIfValid(String token);

    boolean isTokenValid(String token);

    TemporaryToken getTokenByBookingId(UUID bookingId);

    Map<UUID, List<TemporaryTokenResponseDto>> getAllTokens();

    void deactivateIfExpired(TemporaryToken temporaryToken);

    void deactivateTokenByBookingId(UUID bookingId);

    void deactivateAllTokens();

    void regenerateToken(Booking booking, LocalDate newExpiresAt);

}
