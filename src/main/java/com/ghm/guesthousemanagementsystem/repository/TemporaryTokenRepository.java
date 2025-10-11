package com.ghm.guesthousemanagementsystem.repository;

import com.ghm.guesthousemanagementsystem.entity.Booking;
import com.ghm.guesthousemanagementsystem.entity.TemporaryToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TemporaryTokenRepository extends JpaRepository<TemporaryToken, UUID> {

    Optional<TemporaryToken> findByToken(String token);

    TemporaryToken findByBooking(Booking booking);

    Optional<TemporaryToken> findByBooking_BookingId(UUID bookingId);

    @Modifying
    @Query("UPDATE TemporaryToken t SET t.isActive=false")
    void deactivateAllTokens();
}
