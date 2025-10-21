package com.naveen.guesthousemanagementsystem.service;


import com.naveen.guesthousemanagementsystem.dto.addon.AddonSummaryResponseDto;
import com.naveen.guesthousemanagementsystem.dto.bookingaddon.BookingAddonResponseDto;
import com.naveen.guesthousemanagementsystem.entity.Addon;
import com.naveen.guesthousemanagementsystem.entity.Booking;
import com.naveen.guesthousemanagementsystem.entity.BookingAddon;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface BookingAddonService {

    void createBookingAddon(Booking booking, List<UUID> addonIds);

    void createBookingAddon(UUID bookingId, List<UUID> addonIds);

    Map<UUID, List<AddonSummaryResponseDto>> getAddonsForMultipleBookings(List<UUID> bookingIds);

    AddonSummaryResponseDto convertToAddonResponseDto(BookingAddon bookingAddon);

    BigDecimal calculateAddonsTotal(UUID bookingId);

    void cleanupAddonsForBooking(UUID bookingId);

    List<BookingAddon> getAllBookingAddonsWithDetails();

    List<Booking> getAllBookingsWithAddons();

    long getTotalBookingAddonCount();

    Booking findBookingEntityById(UUID bookingId);

    BookingAddonResponseDto convertToResponseDto(BookingAddon bookingAddon);

    List<Addon> getAddonsByBookingId(UUID bookingId);

    Map<UUID, List<Addon>> getAddonsByBookingIds(List<UUID> bookingIds);
}
