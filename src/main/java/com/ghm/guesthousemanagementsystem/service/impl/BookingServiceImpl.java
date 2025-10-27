package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.dto.PropertySummaryDto;
import com.ghm.guesthousemanagementsystem.dto.booking.*;
import com.ghm.guesthousemanagementsystem.dto.RoomLineItemDto;
import com.ghm.guesthousemanagementsystem.entity.*;
import com.ghm.guesthousemanagementsystem.enums.BookingStatus;
import com.ghm.guesthousemanagementsystem.exception.ResourceNotFoundException;
import com.ghm.guesthousemanagementsystem.exception.UnauthorizedAccessException;
import com.ghm.guesthousemanagementsystem.helper.PricingCalculator;
import com.ghm.guesthousemanagementsystem.helper.ReferenceGenerator;
import com.ghm.guesthousemanagementsystem.helper.TokenGenerator;
import com.ghm.guesthousemanagementsystem.mapper.BookingMapper;
import com.ghm.guesthousemanagementsystem.mapper.PropertyMapper;
import com.ghm.guesthousemanagementsystem.mapper.RoomMapper;
import com.ghm.guesthousemanagementsystem.repository.*;
import com.ghm.guesthousemanagementsystem.service.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ghm.guesthousemanagementsystem.mapper.BookingMapper.mapToSummaryResponseDto;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final RoomRepository roomRepository;
    private final BookingRoomRepository bookingRoomRepository;
    private final BookingRoomService bookingRoomService;
    private final BookingStatusHistoryService bookingStatusHistoryService;
    private final TemporaryTokenService temporaryTokenService;

    @Override
    @Transactional
    public BookingAdminResponseDto createBookingAsAdmin(BookingCreateRequestDto createDto) {
        // Fetch requested rooms and validate existence
        List<UUID> roomIds = createDto.getRoomIds();
        List<Room> rooms = roomRepository.findAllById(roomIds);

        // Validate property existence
        Property property = propertyRepository.findById(createDto.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        // Validate booking date logic
        validateBookingDates(createDto.getCheckInDate(), createDto.getCheckOutDate());

        // Ensure at least one room is selected
        validateRoomsNotEmpty(rooms);

        // Ensure all requested rooms exist
        validateAllRoomsExist(rooms, roomIds);

        // Check room availability for the requested dates
        bookingRoomService.validateRoomAvailability(roomIds, createDto.getCheckInDate(), createDto.getCheckOutDate(), null);

        // Validate guest capacity against room capacities
        validateRoomCapacity(rooms, createDto.getNoOfGuests());

        // Map DTO to Booking entity
        Booking booking = BookingMapper.mapCreateDtoToBooking(createDto, property);

        // Generate unique identifiers and calculate pricing
        String referenceId = ReferenceGenerator.generateReferenceId();
        BigDecimal totalPrice = PricingCalculator.calculateTotal(rooms, createDto.getCheckInDate(), createDto.getCheckOutDate());
        String token = TokenGenerator.generateToken();
        LocalDateTime now = LocalDateTime.now();

        // Set booking entity properties
        booking.setReferenceId(referenceId);
        booking.setCreatedAt(now);
        booking.setUpdatedAt(now);
        booking.setStatus(BookingStatus.pending);
        booking.setTotalPrice(totalPrice);
        booking.setPaid(false);
        booking.setExpiredAt(now.plusDays(14));
        booking.setToken(token);

        // Persist booking entity
        bookingRepository.save(booking);

        // Create booking-room associations
        bookingRoomService.createBookingRooms(booking, rooms, createDto.getCheckInDate(), createDto.getCheckOutDate());

        // Record status change history
        bookingStatusHistoryService.statusChange(booking, null, BookingStatus.pending, null);

        // Generate temporary access token
        temporaryTokenService.createTemporaryToken(booking, token, createDto.getCheckOutDate().plusMonths(1));

        // Build and return response DTO
        return buildAdminResponseDto(booking, rooms);
    }

    @Override
    @Transactional
    public BookingAdminResponseDto updateBooking(UUID bookingId, BookingUpdateRequestDto updateDto) {
        // Fetch existing booking and validate it's not cancelled
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        validateBookingNotCancelled(booking);

        // Fetch requested rooms and validate
        List<Room> requestedNewRooms = roomRepository.findAllById(updateDto.getRoomIds());
        validateBookingDates(updateDto.getCheckInDate(), updateDto.getCheckOutDate());
        validateRoomsNotEmpty(requestedNewRooms);
        validateAllRoomsExist(requestedNewRooms, updateDto.getRoomIds());

        // Clear existing rooms
        bookingRoomRepository.deleteAllByBooking(booking);

        // Validate room availability for new selection
        bookingRoomService.validateRoomAvailability(
                requestedNewRooms.stream().map(Room::getId).toList(),
                updateDto.getCheckInDate(),
                updateDto.getCheckOutDate(),
                bookingId);

        // Recalculate total price based on new room selection
        BigDecimal totalPrice = PricingCalculator.calculateTotal(requestedNewRooms , updateDto.getCheckInDate(), updateDto.getCheckOutDate());

        // Validate guest capacity
        validateRoomCapacity(requestedNewRooms, updateDto.getNoOfGuests());

        // Update booking entity with new data
        BookingMapper.mapUpdateDtoToBooking(updateDto, booking);
        booking.setUpdatedAt(LocalDateTime.now());
        booking.setTotalPrice(totalPrice);
        bookingRepository.save(booking);

        // Create new booking-room associations
        List<BookingRoom> bookingRooms = requestedNewRooms.stream()
                .map(room -> new BookingRoom(booking, room, updateDto.getCheckInDate(), updateDto.getCheckOutDate()))
                .toList();
        bookingRoomRepository.saveAll(bookingRooms);

        // Record status update history
        bookingStatusHistoryService.statusChange(booking, BookingStatus.pending, BookingStatus.pending, "Admin updated booking");

        return buildAdminResponseDto(booking, requestedNewRooms);
    }

    @Override
    @Transactional
    public BookingAdminResponseDto confirmBooking(UUID bookingId) {
        // Fetch booking and validate it's not cancelled
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        validateBookingNotCancelled(booking);

        // Fetch associated rooms
        List<Room> rooms = bookingRoomRepository.findAllByBooking(booking).stream()
                .map(BookingRoom::getRoom)
                .toList();

        // Update booking status to confirmed
        LocalDateTime now = LocalDateTime.now();
        booking.setStatus(BookingStatus.confirmed);
        booking.setPaid(true);
        booking.setConfirmedAt(now);
        bookingRepository.save(booking);

        // Record status change history
        bookingStatusHistoryService.statusChange(booking, BookingStatus.pending, BookingStatus.confirmed, "Admin confirmed the booking");

        // Build and return response DTO
        return buildAdminResponseDto(booking, rooms);
    }

    @Override
    @Transactional
    public BookingAdminResponseDto cancelBookingAsAdmin(UUID bookingId, BookingCancelRequestDto cancelDto) {
        // Fetch booking and validate it's not already cancelled
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        if (booking.getStatus() == BookingStatus.cancelled) {
            throw new IllegalStateException("Booking is already cancelled");
        }

        // Fetch associated addons and rooms
        List<Room> rooms = bookingRoomRepository.findAllByBooking_BookingId(bookingId).stream()
                .map(BookingRoom::getRoom)
                .toList();

        // Update booking status to cancelled
        LocalDateTime now = LocalDateTime.now();
        booking.setStatus(BookingStatus.cancelled);
        booking.setUpdatedAt(now);
        bookingRepository.save(booking);

        // Clear room and addon associations
        bookingRoomRepository.deleteAllByBooking(booking);

        // Record cancellation in history
        bookingStatusHistoryService.statusChange(booking, BookingStatus.pending, BookingStatus.cancelled, cancelDto.getReason());

        // Build and return response DTO
        return buildAdminResponseDto(booking, rooms);
    }

    @Override
    @Transactional
    public BookingAdminResponseDto attachRooms(UUID bookingId, BookingAttachRoomsRequestDto attachRoomsDto) {
        // Fetch booking and validate it's not cancelled
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        validateBookingNotCancelled(booking);

        // Fetch current rooms and addons
        List<Room> currentRooms = bookingRoomRepository.findAllByBooking_BookingId(bookingId).stream()
                .map(BookingRoom::getRoom)
                .toList();

        // Fetch and validate new rooms to attach
        List<Room> newRooms = roomRepository.findAllById(attachRoomsDto.getRoomIds());
        validateAllRoomsExist(newRooms, attachRoomsDto.getRoomIds());

        // Filter out rooms that are already attached
        List<Room> roomsToAttach = newRooms.stream()
                .filter(room -> !currentRooms.contains(room))
                .toList();
        if (roomsToAttach.isEmpty()) {
            throw new IllegalArgumentException("No new rooms to attach");
        }

        // Validate availability for new rooms
        bookingRoomService.validateRoomAvailability(
                roomsToAttach.stream().map(Room::getId).toList(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                null);

        // Create booking-room associations for new rooms
        List<BookingRoom> bookingRooms = roomsToAttach.stream()
                .map(room -> new BookingRoom(booking, room, booking.getCheckInDate(), booking.getCheckOutDate()))
                .toList();
        bookingRoomRepository.saveAll(bookingRooms);

        // Record status update history
        bookingStatusHistoryService.statusChange(booking, BookingStatus.pending, BookingStatus.pending, "Admin attached rooms");

        // Recalculate total price with all rooms
        List<Room> totalNewRooms = Stream.concat(currentRooms.stream(), roomsToAttach.stream()).toList();
        BigDecimal totalPrice = PricingCalculator.calculateTotal(totalNewRooms, booking.getCheckInDate(), booking.getCheckOutDate());

        // Update booking with new total price
        booking.setTotalPrice(totalPrice);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        // Build and return response DTO
        return buildAdminResponseDto(booking, totalNewRooms);
    }

    @Override
    public BookingAdminResponseDto getBookingById(UUID bookingId) {
        // Fetch booking by ID
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // Fetch associated rooms and addons
        List<Room> rooms = bookingRoomRepository.findAllByBooking_BookingId(bookingId).stream()
                .map(BookingRoom::getRoom)
                .toList();

        // Build and return response DTO
        return buildAdminResponseDto(booking, rooms);
    }

    @Override
    public List<BookingAdminResponseDto> getAllBookingsAsAdmin() {
        // Fetch all bookings
        List<Booking> bookings = bookingRepository.findAll();

        // Extract booking IDs for batch processing
        List<UUID> bookingIds = bookings.stream()
                .map(Booking::getBookingId)
                .toList();

        // Batch fetch rooms and addons for all bookings
        Map<UUID, List<Room>> roomsByBookingId = bookingRoomService.mapRoomsByBookingIds(bookingIds);

        // Convert each booking to response DTO
        return bookings.stream()
                .map(booking -> {
                    List<Room> rooms = roomsByBookingId.getOrDefault(booking.getBookingId(), List.of());
                    return buildAdminResponseDto(booking, rooms);
                })
                .toList();
    }

    @Override
    public List<BookingSummaryResponseDto> getAllBookingSummariesAsAdmin() {
        // Fetch all bookings
        List<Booking> bookings = bookingRepository.findAll();

        // Extract booking IDs for batch processing
        List<UUID> bookingIds = bookings.stream()
                .map(Booking::getBookingId)
                .collect(Collectors.toList());

        // Convert each booking to summary response DTO
        return bookings.stream()
                .map(booking -> {
                    return mapToSummaryResponseDto(booking);
                })
                .toList();
    }

    @Override
    @Transactional
    public BookingGuestResponseDto createBookingAsGuest(BookingCreateRequestDto createDto) {
        // Fetch requested rooms and validate
        List<UUID> roomIds = createDto.getRoomIds();
        List<Room> rooms = roomRepository.findAllById(roomIds);

        // Validate property existence
        Property property = propertyRepository.findById(createDto.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        // Validate booking date logic
        validateBookingDates(createDto.getCheckInDate(), createDto.getCheckOutDate());

        // Ensure at least one room is selected
        validateRoomsNotEmpty(rooms);

        // Ensure all requested rooms exist
        validateAllRoomsExist(rooms, roomIds);

        // Check room availability
        bookingRoomService.validateRoomAvailability(roomIds, createDto.getCheckInDate(), createDto.getCheckOutDate(), null);

        // Validate guest capacity
        validateRoomCapacity(rooms, createDto.getNoOfGuests());

        // Map DTO to Booking entity
        Booking booking = BookingMapper.mapCreateDtoToBooking(createDto, property);

        // Generate unique identifiers and calculate pricing
        String referenceId = ReferenceGenerator.generateReferenceId();
        BigDecimal totalPrice = PricingCalculator.calculateTotal(rooms, createDto.getCheckInDate(), createDto.getCheckOutDate());
        String token = TokenGenerator.generateToken();
        LocalDateTime now = LocalDateTime.now();

        // Set booking entity properties
        booking.setReferenceId(referenceId);
        booking.setCreatedAt(now);
        booking.setUpdatedAt(now);
        booking.setStatus(BookingStatus.pending);
        booking.setTotalPrice(totalPrice);
        booking.setPaid(false);
        booking.setExpiredAt(now.plusDays(14));
        booking.setToken(token);

        // Persist booking entity
        bookingRepository.save(booking);

        // Create booking-room associations
        bookingRoomService.createBookingRooms(booking, rooms, createDto.getCheckInDate(), createDto.getCheckOutDate());

        // Record status change history
        bookingStatusHistoryService.statusChange(booking, null, BookingStatus.pending, null);

        // Generate temporary access token
        temporaryTokenService.createTemporaryToken(booking, token, createDto.getCheckOutDate().plusMonths(1));

        // Build and return guest response DTO with addons
        return buildGuestResponseDto(booking, rooms);
    }

    @Override
    @Transactional
    public BookingGuestResponseDto patchBooking(String referenceId, BookingPatchRequestDto patchDto) {
        // Fetch booking by reference ID and validate it's not cancelled
        Booking booking = bookingRepository.findByReferenceId(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        validateBookingNotCancelled(booking);

        // Fetch associated rooms and addons for validation and response
        List<Room> rooms = bookingRoomRepository.findAllByBooking_BookingId(booking.getBookingId()).stream()
                .map(BookingRoom::getRoom)
                .toList();

        // Validate and update number of guests if provided
        if (patchDto.getNoOfGuests() != null) {
            validateRoomCapacity(rooms, patchDto.getNoOfGuests());
            booking.setNoOfGuests(patchDto.getNoOfGuests());
        }

        // Apply partial updates to booking entity
        BookingMapper.mapPatchDtoToBooking(patchDto, booking);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        // Record update in status history
        bookingStatusHistoryService.statusChange(booking, BookingStatus.pending, BookingStatus.pending, "Guest patched the booking");

        // Build and return response DTO with addons
        return buildGuestResponseDto(booking, rooms);
    }

    @Override
    @Transactional
    public BookingGuestResponseDto amendBooking(String referenceId, BookingAmendRequestDto amendDto) {
        // Fetch booking by reference ID and validate it's not cancelled
        Booking booking = bookingRepository.findByReferenceId(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        validateBookingNotCancelled(booking);

        if(booking.getStatus().equals(BookingStatus.confirmed)){
            throw new UnauthorizedAccessException("Booking is already confirmed");
        }

        // Fetch associated rooms and addons
        List<Room> rooms = bookingRoomRepository.findAllByBooking_BookingId(booking.getBookingId()).stream()
                .map(BookingRoom::getRoom)
                .toList();

        List<UUID> roomIds = rooms.stream().map(Room::getId).toList();

        // Validate new booking dates
        validateBookingDates(amendDto.getNewCheckInDate(), amendDto.getNewCheckOutDate());

        // Check room availability for new dates
        bookingRoomService.validateRoomAvailability(roomIds, amendDto.getNewCheckInDate(), amendDto.getNewCheckOutDate(), booking.getBookingId());

        // Apply date changes to booking entity
        BookingMapper.mapAmendDtoToBooking(amendDto, booking);

        // Recalculate total price for new dates
        BigDecimal totalPrice = PricingCalculator.calculateTotal(rooms, amendDto.getNewCheckInDate(), amendDto.getNewCheckOutDate());
        booking.setUpdatedAt(LocalDateTime.now());
        booking.setTotalPrice(totalPrice);
        bookingRepository.save(booking);

        // Update booking-room associations with new dates
        bookingRoomService.updateBookingRooms(booking, rooms, amendDto.getNewCheckInDate(), amendDto.getNewCheckOutDate());

        // Record amendment in status history
        bookingStatusHistoryService.statusChange(booking, BookingStatus.pending, BookingStatus.pending, "Guest amended the booking");

        // Build and return response DTO with addons
        return buildGuestResponseDto(booking, rooms);
    }

    @Override
    @Transactional
    public BookingGuestResponseDto cancelBookingAsGuest(String referenceId, BookingCancelRequestDto cancelDto) {
        // Fetch booking by reference ID
        Booking booking = bookingRepository.findByReferenceId(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // Validate booking is not already cancelled
        if (booking.getStatus() == BookingStatus.cancelled) {
            throw new IllegalStateException("Booking is already cancelled");
        }

        // Prevent cancellation after check-in date
        if (LocalDate.now().isAfter(booking.getCheckInDate())) {
            throw new IllegalStateException("Guest cannot cancel after check-in date");
        }

        // Fetch associated rooms and addons for response
        List<Room> rooms = bookingRoomRepository.findAllByBooking_BookingId(booking.getBookingId()).stream()
                .map(BookingRoom::getRoom)
                .toList();

        // Record cancellation in status history
        bookingStatusHistoryService.statusChange(booking, BookingStatus.pending, BookingStatus.cancelled, cancelDto.getReason());

        // Clear room and addon associations
        bookingRoomRepository.deleteAllByBooking(booking);

        // Update booking status
        booking.setStatus(BookingStatus.cancelled);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        // Build and return response DTO with addons
        return buildGuestResponseDto(booking, rooms);
    }

    @Override
    public BookingGuestResponseDto getBookingByReferenceId(String referenceId) {
        // Fetch booking by reference ID
        Booking booking = bookingRepository.findByReferenceId(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // Validate temporary token is still valid
        if (!temporaryTokenService.isTokenValid(booking.getToken())) {
            throw new IllegalArgumentException("Booking can't be accessed");
        }

        // Fetch associated rooms and addons
        List<Room> rooms = bookingRoomRepository.findAllByBooking_BookingId(booking.getBookingId()).stream()
                .map(BookingRoom::getRoom)
                .toList();

        // Build and return response DTO with addons
        return buildGuestResponseDto(booking, rooms);
    }


    //Map to admin response dto
    private BookingAdminResponseDto buildAdminResponseDto(Booking booking, List<Room> rooms) {

        // Map property to summary DTO
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());

        // Map rooms to line item DTOs
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms, booking.getCheckInDate(), booking.getCheckOutDate());

        // Combine all into admin response DTO
        return BookingMapper.mapBookingToAdminResponseDto(booking, propertySummaryDto, roomLineItemDtos);
    }

    //Map to guest response dto
    private BookingGuestResponseDto buildGuestResponseDto(Booking booking, List<Room> rooms) {

        // Map property to summary DTO
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());

        // Map rooms to line item DTOs
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms, booking.getCheckInDate(), booking.getCheckOutDate());

        // Combine all into guest response DTO
        return BookingMapper.mapBookingToGuestResponseDto(booking, propertySummaryDto, roomLineItemDtos);
    }


    //Validating check in date < check out date
    private void validateBookingDates(LocalDate checkIn, LocalDate checkOut) {
        // Ensure check-out date is after check-in date
        if (!checkIn.isBefore(checkOut)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }
    }

    //validating room list is not empty
    private void validateRoomsNotEmpty(List<Room> rooms) {
        // Ensure rooms list is not empty
        if (rooms.isEmpty()) {
            throw new IllegalArgumentException("No valid rooms found for the provided id");
        }
    }

    //Check if all selected rooms are valid
    private void validateAllRoomsExist(List<Room> rooms, List<UUID> roomIds) {
        // Ensure all requested room IDs were found
        if (rooms.size() != roomIds.size()) {
            throw new ResourceNotFoundException("One or more rooms not found");
        }
    }

    //Check total guest count is less or equal to total room capacity
    private void validateRoomCapacity(List<Room> rooms, int numberOfGuests) {
        // Calculate total capacity of all selected rooms
        int totalCapacity = rooms.stream().mapToInt(Room::getMaxOccupancy).sum();
        // Ensure capacity is sufficient for number of guests
        if (numberOfGuests > totalCapacity) {
            throw new IllegalArgumentException("Selected no. of rooms cannot accommodate the number of guests");
        }
    }

    //Validate of booking has been cancelled
    private void validateBookingNotCancelled(Booking booking) {
        // Prevent operations on cancelled bookings
        if (booking.getStatus().equals(BookingStatus.cancelled)) {
            throw new UnauthorizedAccessException("Booking is cancelled");
        }
    }

}