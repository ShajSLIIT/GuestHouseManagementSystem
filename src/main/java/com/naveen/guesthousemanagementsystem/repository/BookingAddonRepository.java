package com.naveen.guesthousemanagementsystem.repository;

import com.naveen.guesthousemanagementsystem.entity.BookingAddon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingAddonRepository extends JpaRepository<BookingAddon, UUID> {

    /** Find all booking addons for a specific booking */
    List<BookingAddon> findByBookingId(Long bookingId);

    /** Find all booking addons for a specific addon */
    List<BookingAddon> findByAddonId(UUID addonId);

    /** Check if booking addon exists for booking and addon */
    boolean existsByBookingIdAndAddonId(Long bookingId, UUID addonId);

    /** Delete all booking addons for a specific booking */
    void deleteByBookingId(Long bookingId);

    /** Delete specific booking addon by booking ID and addon ID */
    void deleteByBookingIdAndAddonId(Long bookingId, UUID addonId);

    /** Find booking addons for multiple booking IDs */
    @Query("SELECT ba FROM BookingAddon ba WHERE ba.bookingId IN :bookingIds")
    List<BookingAddon> findByBookingIds(@Param("bookingIds") List<Long> bookingIds);
}