package com.naveen.guesthousemanagementsystem.repository;


import com.naveen.guesthousemanagementsystem.entity.Booking;
import com.naveen.guesthousemanagementsystem.entity.BookingAddon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingAddonRepository extends JpaRepository<BookingAddon, UUID> {
    List<BookingAddon> findByBooking(Booking booking);

    @Query("SELECT ba FROM BookingAddon ba JOIN FETCH ba.addon WHERE ba.booking.bookingId = :bookingId")
    List<BookingAddon> findByBookingIdWithAddon(@Param("bookingId") UUID bookingId);

    boolean existsByBookingAndAddonId(Booking booking, UUID addonId);

    void deleteByBooking(Booking booking);

    void deleteByBookingBookingId(UUID bookingId);

    @Query("SELECT ba FROM BookingAddon ba JOIN FETCH ba.addon JOIN FETCH ba.booking")
    List<BookingAddon> findAllWithDetails();

    @Query("SELECT DISTINCT ba.booking FROM BookingAddon ba")
    List<Booking> findDistinctBookingsWithAddons();

    long countByBooking(Booking booking);


    // In BookingAddonRepository
    @Query("SELECT ba FROM BookingAddon ba JOIN FETCH ba.addon WHERE ba.booking.bookingId IN :bookingIds")
    List<BookingAddon> findByBookingIdIn(@Param("bookingIds") List<UUID> bookingIds);

    List<BookingAddon> findAllByBooking_BookingId(UUID bookingId);
}