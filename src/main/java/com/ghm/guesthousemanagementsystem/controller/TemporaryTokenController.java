package com.ghm.guesthousemanagementsystem.controller;

import com.ghm.guesthousemanagementsystem.dto.temporarytoken.TemporaryTokenRequestDto;
import com.ghm.guesthousemanagementsystem.dto.temporarytoken.TemporaryTokenResponseDto;
import com.ghm.guesthousemanagementsystem.entity.Booking;
import com.ghm.guesthousemanagementsystem.entity.TemporaryToken;
import com.ghm.guesthousemanagementsystem.exception.ResourceNotFoundException;
import com.ghm.guesthousemanagementsystem.repository.BookingRepository;
import com.ghm.guesthousemanagementsystem.service.TemporaryTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/temporary-token/admin")
@RequiredArgsConstructor
public class TemporaryTokenController {

    private final TemporaryTokenService temporaryTokenService;

    private final BookingRepository bookingRepository;

    @GetMapping("/by-token/{token}")
    public ResponseEntity<TemporaryToken> getTokenIfValid(@PathVariable String token){
        TemporaryToken temporaryToken = temporaryTokenService.getTokenIfValid(token);
        return ResponseEntity.ok(temporaryToken);
    }

    @GetMapping("/by-booking/{bookingId}")
    public ResponseEntity<TemporaryToken> getTokenByBookingId(@PathVariable UUID bookingId){
        TemporaryToken temporaryToken = temporaryTokenService.getTokenByBookingId(bookingId);
        return ResponseEntity.ok(temporaryToken);
    }

    @GetMapping("/all-tokens")
    public ResponseEntity<Map<UUID, List<TemporaryTokenResponseDto>>> getAllTokens(){
        Map<UUID, List<TemporaryTokenResponseDto>> tokenMap = temporaryTokenService.getAllTokens();
        return ResponseEntity.ok(tokenMap);
    }

    @PatchMapping("/{bookingId}/deactivate")
    public ResponseEntity<String> deactivateBookingByBookingId(@PathVariable UUID  bookingId){
        temporaryTokenService.deactivateTokenByBookingId(bookingId);
        return ResponseEntity.ok("Token has been deactivated for booking id: " + bookingId);
    }

    @PatchMapping("/deactivate-all")
    public ResponseEntity<String> deactivateAllTokens(){
        temporaryTokenService.deactivateAllTokens();
        return ResponseEntity.ok("Deactivated all tokens");
    }

    @PostMapping("/regenerate")
    public ResponseEntity<String> regenerateToken(@RequestBody TemporaryTokenRequestDto requestDto) {
        Booking booking = bookingRepository.findByBookingId(requestDto.getBookingId())
                .orElseThrow(()-> new ResourceNotFoundException("Booking not found"));
        temporaryTokenService.regenerateToken(booking, requestDto.getNewExpiresAt());
        return ResponseEntity.ok("Token regenerated for booking id:" + requestDto.getBookingId());
    }

}
