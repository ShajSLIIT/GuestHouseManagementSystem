package com.naveen.guesthousemanagementsystem.mapper;


import com.naveen.guesthousemanagementsystem.dto.addon.AddonSummaryResponseDto;
import com.naveen.guesthousemanagementsystem.dto.booking.*;
import com.naveen.guesthousemanagementsystem.entity.Booking;

import java.util.List;

public class BookingMapper {

    public static Booking mapCreateDtoToBooking(BookingCreateRequestDto createDto, Property property) {
        Booking newCreateBooking = new Booking();
        newCreateBooking.setGuestName(createDto.getGuestName());
        newCreateBooking.setGuestEmail(createDto.getGuestEmail());
        newCreateBooking.setGuestPhone(createDto.getGuestPhone());
        newCreateBooking.setCheckInDate(createDto.getCheckInDate());
        newCreateBooking.setCheckOutDate(createDto.getCheckOutDate());
        newCreateBooking.setNoOfRooms(createDto.getNoOfRooms());
        newCreateBooking.setNoOfGuests(createDto.getNoOfGuests());
        newCreateBooking.setNotes(createDto.getNotes());
        newCreateBooking.setProperty(property);
        return newCreateBooking;
    }

    public static void mapUpdateDtoToBooking(BookingUpdateRequestDto updateDto, Booking booking) {
        booking.setGuestName(updateDto.getGuestName());
        booking.setGuestEmail(updateDto.getGuestEmail());
        booking.setGuestPhone(updateDto.getGuestPhone());
        booking.setCheckInDate(updateDto.getCheckInDate());
        booking.setCheckOutDate(updateDto.getCheckOutDate());
        booking.setNoOfRooms(updateDto.getNoOfRooms());
        booking.setNoOfGuests(updateDto.getNoOfGuests());
        booking.setNotes(updateDto.getNotes());
    }

    public static void mapPatchDtoToBooking(BookingPatchRequestDto patchDto, Booking booking) {
        if (patchDto.getGuestName() != null) booking.setGuestName(patchDto.getGuestName());
        if (patchDto.getGuestEmail() != null) booking.setGuestEmail(patchDto.getGuestEmail());
        if (patchDto.getGuestPhone() != null) booking.setGuestPhone(patchDto.getGuestPhone());
        if (patchDto.getNoOfGuests() != null) booking.setNoOfGuests(patchDto.getNoOfGuests());
        if (patchDto.getNotes() != null) booking.setNotes(patchDto.getNotes());
    }

    public static void mapAmendDtoToBooking(BookingAmendRequestDto amendDto, Booking booking) {
        booking.setCheckInDate(amendDto.getNewCheckInDate());
        booking.setCheckOutDate(amendDto.getNewCheckOutDate());
//        booking.setNoOfGuests(amendDto.getNewNoOfGuests());
    }

    public static BookingAdminResponseDto mapBookingToAdminResponseDto(Booking booking,
                                                                       PropertySummaryDto propertySummaryDto,
                                                                       List<RoomLineItemDto> rooms,
                                                                       List<AddonSummaryResponseDto> addonSummary) {
        BookingAdminResponseDto newResponseDto = new BookingAdminResponseDto();

        newResponseDto.setBookingId(booking.getBookingId());
        newResponseDto.setProperty(propertySummaryDto);
        newResponseDto.setRooms(rooms);
        newResponseDto.setAddons(addonSummary);
        newResponseDto.setReferenceId(booking.getReferenceId());
        newResponseDto.setGuestName(booking.getGuestName());
        newResponseDto.setGuestEmail(booking.getGuestEmail());
        newResponseDto.setGuestPhone(booking.getGuestPhone());
        newResponseDto.setCheckInDate(booking.getCheckInDate());
        newResponseDto.setCheckOutDate(booking.getCheckOutDate());
        newResponseDto.setCreatedAt(booking.getCreatedAt());
        newResponseDto.setUpdatedAt(booking.getUpdatedAt());
        newResponseDto.setStatus(booking.getStatus());
        newResponseDto.setNoOfRooms(booking.getNoOfRooms());
        newResponseDto.setNoOfGuests(booking.getNoOfGuests());
        newResponseDto.setTotalPrice(booking.getTotalPrice());
//        newResponseDto.setCustomerUniqueId(booking.getCustomerUniqueId());
        newResponseDto.setPaid(booking.isPaid());
        newResponseDto.setConfirmedAt(booking.getConfirmedAt());
        newResponseDto.setExpiredAt(booking.getExpiredAt());
        newResponseDto.setNotes(booking.getNotes());
        newResponseDto.setToken(booking.getToken());

        return newResponseDto;
    }

    public static BookingGuestResponseDto mapBookingToGuestResponseDto(Booking booking,
                                                                       PropertySummaryDto propertySummaryDto,
                                                                       List<RoomLineItemDto> rooms,
                                                                       List<AddonSummaryResponseDto> addonSummary) {
        BookingGuestResponseDto newResponseDto = new BookingGuestResponseDto();

        newResponseDto.setProperty(propertySummaryDto);
        newResponseDto.setRooms(rooms);
        newResponseDto.setAddons(addonSummary);
        newResponseDto.setReferenceId(booking.getReferenceId());
        newResponseDto.setGuestName(booking.getGuestName());
        newResponseDto.setGuestEmail(booking.getGuestEmail());
        newResponseDto.setGuestPhone(booking.getGuestPhone());
        newResponseDto.setCheckInDate(booking.getCheckInDate());
        newResponseDto.setCheckOutDate(booking.getCheckOutDate());
        newResponseDto.setCreatedAt(booking.getCreatedAt());
        newResponseDto.setStatus(booking.getStatus());
        newResponseDto.setNoOfRooms(booking.getNoOfRooms());
        newResponseDto.setNoOfGuests(booking.getNoOfGuests());
        newResponseDto.setTotalPrice(booking.getTotalPrice());
        newResponseDto.setConfirmedAt(booking.getConfirmedAt());
        newResponseDto.setNotes(booking.getNotes());

        return newResponseDto;
    }

    public static BookingSummaryResponseDto mapToSummaryResponseDto(Booking booking) {
        BookingSummaryResponseDto newResponseDto = new BookingSummaryResponseDto();

        newResponseDto.setBookingId(booking.getBookingId());
        newResponseDto.setReferenceId(booking.getReferenceId());
        newResponseDto.setGuestName(booking.getGuestName());
        newResponseDto.setCheckInDate(booking.getCheckInDate());
        newResponseDto.setCheckOutDate(booking.getCheckOutDate());
        newResponseDto.setStatus(booking.getStatus());
        newResponseDto.setTotalPrice(booking.getTotalPrice());

        return newResponseDto;
    }

}
