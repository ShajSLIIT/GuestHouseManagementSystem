package com.naveen.guesthousemanagementsystem.service.impl;


import com.naveen.guesthousemanagementsystem.dto.addon.AddonSummaryResponseDto;
import com.naveen.guesthousemanagementsystem.dto.bookingaddon.BookingAddonResponseDto;
import com.naveen.guesthousemanagementsystem.entity.Addon;
import com.naveen.guesthousemanagementsystem.entity.Booking;
import com.naveen.guesthousemanagementsystem.entity.BookingAddon;
import com.naveen.guesthousemanagementsystem.exeption.ResourceNotFoundException;
import com.naveen.guesthousemanagementsystem.repository.BookingAddonRepository;
import com.naveen.guesthousemanagementsystem.repository.BookingRepository;
import com.naveen.guesthousemanagementsystem.service.BookingAddonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingAddonServiceImpl implements BookingAddonService {
    private final BookingAddonRepository bookingAddonRepository;
    private final AddonServiceImpl addonServiceImpl;
    private final BookingRepository bookingRepository;  // ← Use repository instead of service

    // ====================== MAIN METHOD: Attach addons using Booking entity ======================
    @Transactional
    @Override
    public void createBookingAddon(Booking booking, List<UUID> addonIds) {
        log.info("Attaching {} addons to booking: {}",
                addonIds != null ? addonIds.size() : 0, booking.getBookingId());

        if (addonIds == null || addonIds.isEmpty()) return;

        for (UUID addonId : addonIds) {
            var addon = addonServiceImpl.findAddonEntityById(addonId);

            if (!addon.getActive()) {
                log.warn("Skipping inactive addon: {} for booking: {}", addon.getName(), booking.getBookingId());
                continue;
            }

            // Check for duplicates using Booking entity
            if (!bookingAddonRepository.existsByBookingAndAddonId(booking, addonId)) {
                BookingAddon bookingAddon = BookingAddon.builder()
                        .booking(booking)  // ← Using Booking entity now
                        .addon(addon)
                        .build();

                bookingAddonRepository.save(bookingAddon);
                log.debug("Attached addon: {} to booking: {}", addon.getName(), booking.getBookingId());
            }
        }

        log.info("Successfully attached addons to booking: {}", booking.getBookingId());
    }

    // ====================== OVERLOADED METHOD: For backward compatibility ======================
    @Transactional
    @Override
    public void createBookingAddon(UUID bookingId, List<UUID> addonIds) {
        Booking booking = findBookingEntityById(bookingId);  // Use local method
        createBookingAddon(booking, addonIds);
    }

    // ====================== GET ADDONS FOR BOOKING ======================
    @Override
    public Map<UUID, List<AddonSummaryResponseDto>> getAddonsForMultipleBookings(List<UUID> bookingIds) {
        log.debug("Fetching addons for multiple booking IDs: {}", bookingIds);

        if (bookingIds == null || bookingIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<BookingAddon> bookingAddons = bookingAddonRepository.findByBookingIdIn(bookingIds);

        return bookingAddons.stream()
                .collect(Collectors.groupingBy(
                        bookingAddon -> bookingAddon.getBooking().getBookingId(),
                        Collectors.mapping(this::convertToAddonResponseDto, Collectors.toList())
                ));
    }

    //Should be inside mapper
    @Override
    public AddonSummaryResponseDto convertToAddonResponseDto(BookingAddon bookingAddon) {
        AddonSummaryResponseDto dto = new AddonSummaryResponseDto();
        dto.setName(bookingAddon.getAddon().getName());
        dto.setDescription(bookingAddon.getAddon().getDescription());
        dto.setPrice(bookingAddon.getAddon().getPrice());
        return dto;
    }

    // ====================== CALCULATE ADDONS TOTAL ======================
    @Override
    public BigDecimal calculateAddonsTotal(UUID bookingId) {
        log.debug("Calculating addons total for booking ID: {}", bookingId);
        List<BookingAddon> bookingAddons = bookingAddonRepository.findByBookingIdWithAddon(bookingId);
        return bookingAddons.stream()
                .map(bookingAddon -> bookingAddon.getAddon().getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ====================== CLEANUP METHODS ======================
//    @Transactional
//    public void cleanupAddonsForBooking(Booking booking) {
//        log.info("Cleaning up addons for booking: {}", booking.getBookingId());
//        bookingAddonRepository.deleteByBooking(booking);
//        log.info("Successfully cleaned up addons for booking: {}", booking.getBookingId());
//    }

    @Transactional
    @Override
    public void cleanupAddonsForBooking(UUID bookingId) {
        Booking booking = findBookingEntityById(bookingId);
//        cleanupAddonsForBooking(booking);
        log.info("Cleaning up addons for booking: {}", booking.getBookingId());
        bookingAddonRepository.deleteByBooking(booking);
        log.info("Successfully cleaned up addons for booking: {}", booking.getBookingId());
    }


    // ====================== MONITORING METHODS ======================
    @Override
    public List<BookingAddon> getAllBookingAddonsWithDetails() {
        return bookingAddonRepository.findAllWithDetails();
    }

    @Override
    public List<Booking> getAllBookingsWithAddons() {
        return bookingAddonRepository.findDistinctBookingsWithAddons();
    }

    @Override
    public long getTotalBookingAddonCount() {
        return bookingAddonRepository.count();
    }

    // ====================== PRIVATE HELPER METHOD ======================
    @Override
    public Booking findBookingEntityById(UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Booking not found with id: " + bookingId));
    }

    // ====================== CONVERSION METHOD ======================
    @Override
    public BookingAddonResponseDto convertToResponseDto(BookingAddon bookingAddon) {
        BookingAddonResponseDto dto = new BookingAddonResponseDto();
        dto.setId(bookingAddon.getId());
        dto.setAddonId(bookingAddon.getAddon().getId());
        dto.setAddonName(bookingAddon.getAddon().getName());
        dto.setAddonDescription(bookingAddon.getAddon().getDescription());
        dto.setAddonPrice(bookingAddon.getAddon().getPrice());
        return dto;
    }

    @Override
    public List<Addon> getAddonsByBookingId(UUID bookingId) {
        return bookingAddonRepository.findAllByBooking_BookingId(bookingId)
                .stream()
                .map(BookingAddon::getAddon)
                .collect(Collectors.toList());
    }

    // Also add this method to get addons mapped by booking IDs
    @Override
    public Map<UUID, List<Addon>> getAddonsByBookingIds(List<UUID> bookingIds) {
        List<BookingAddon> bookingAddons = bookingAddonRepository.findByBookingIdIn(bookingIds);

        return bookingAddons.stream()
                .collect(Collectors.groupingBy(
                        bookingAddon -> bookingAddon.getBooking().getBookingId(),
                        Collectors.mapping(BookingAddon::getAddon, Collectors.toList())
                ));
    }
}