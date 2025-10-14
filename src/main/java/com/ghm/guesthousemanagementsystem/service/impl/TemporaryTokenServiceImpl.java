package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.dto.temporarytoken.TemporaryTokenResponseDto;
import com.ghm.guesthousemanagementsystem.entity.Booking;
import com.ghm.guesthousemanagementsystem.entity.TemporaryToken;
import com.ghm.guesthousemanagementsystem.exceptions.ResourceNotFoundException;
import com.ghm.guesthousemanagementsystem.exceptions.TokenInactiveException;
import com.ghm.guesthousemanagementsystem.helper.TokenGenerator;
import com.ghm.guesthousemanagementsystem.mapper.TemporaryTokenMapper;
import com.ghm.guesthousemanagementsystem.repository.BookingRepository;
import com.ghm.guesthousemanagementsystem.repository.TemporaryTokenRepository;
import com.ghm.guesthousemanagementsystem.service.TemporaryTokenService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TemporaryTokenServiceImpl implements TemporaryTokenService {

    private final BookingRepository bookingRepository;
    private final TemporaryTokenRepository temporaryTokenRepository;

    @Override
    public void createTemporaryToken(Booking booking, String token, LocalDate expiresAt) {
        TemporaryToken temporaryToken = new TemporaryToken();

        temporaryToken.setBooking(booking);
        temporaryToken.setToken(token);
        temporaryToken.setExpiresAt(expiresAt);
        temporaryToken.setActive(true);

        temporaryTokenRepository.save(temporaryToken);
    }

    @Override
    @Transactional
    public TemporaryToken getTokenIfValid(String token) {

        TemporaryToken temporaryToken = temporaryTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));

        if(!temporaryToken.isActive()){
            throw new TokenInactiveException("Token is inactive");
        }

        deactivateIfExpired(temporaryToken);

        return temporaryToken;
    }

    @Override
    @Transactional
    public boolean isTokenValid(String token) {

        TemporaryToken temporaryToken = temporaryTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));

        if(temporaryToken.isActive() && temporaryToken.getExpiresAt().isBefore(LocalDate.now())){
            temporaryToken.setActive(false);
            temporaryTokenRepository.save(temporaryToken);
        }

        return temporaryToken.isActive() && !temporaryToken.getExpiresAt().isBefore(LocalDate.now());
    }

    @Override
    public TemporaryToken getTokenByBookingId(UUID bookingId) {

        return temporaryTokenRepository.findByBooking_BookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Token not found for this booking"));
    }

    @Override
    public Map<UUID, List<TemporaryTokenResponseDto>> getAllTokens() {

        List<TemporaryToken> temporaryTokens = temporaryTokenRepository.findAll();

        Map<UUID, List<TemporaryTokenResponseDto>> tokensByBookingId = temporaryTokens.stream()
                .collect(Collectors.groupingBy(
                        token -> token.getBooking().getBookingId(),
                        Collectors.mapping(
                                TemporaryTokenMapper::mapTemporaryTokenToTemporaryTokenResponseDto,
                                Collectors.toList()
                        )
                ));

        return tokensByBookingId;
    }

    @Override
    @Transactional
    public void deactivateIfExpired(TemporaryToken temporaryToken){

        if(temporaryToken.getExpiresAt().isBefore(LocalDate.now())){
            temporaryToken.setActive(false);
            temporaryTokenRepository.save(temporaryToken);
        }
    }

    @Override
    @Transactional
    public void deactivateTokenByBookingId(UUID bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking id not found"));

        TemporaryToken temporaryToken = temporaryTokenRepository.findByBooking(booking);

        if(temporaryToken == null){
            throw new ResourceNotFoundException("Token not found");
        }

        temporaryToken.setActive(false);
        temporaryTokenRepository.save(temporaryToken);
    }

    @Override
    @Transactional
    public void deactivateAllTokens(){
        temporaryTokenRepository.deactivateAllTokens();
    }

    @Override
    @Transactional
    public void regenerateToken(Booking booking, LocalDate newExpiresAt) {
        TemporaryToken oldToken = temporaryTokenRepository.findByBooking(booking);

        if(oldToken == null){
            throw new ResourceNotFoundException("Token not found");
        }

        if(oldToken.isActive()) {
            oldToken.setActive(false);
            temporaryTokenRepository.save(oldToken);
        }

        TemporaryToken newToken = new TemporaryToken();
        newToken.setBooking(booking);
        newToken.setToken(TokenGenerator.generateToken());
        newToken.setExpiresAt(newExpiresAt);
        newToken.setActive(true);

        booking.setToken(newToken.getToken());
        bookingRepository.save(booking);
        temporaryTokenRepository.save(newToken);
    }
}
