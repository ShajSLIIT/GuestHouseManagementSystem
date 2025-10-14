package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.dto.booking.*;
import com.ghm.guesthousemanagementsystem.dto.supportingdto.PropertySummaryDto;
import com.ghm.guesthousemanagementsystem.dto.supportingdto.RoomLineItemDto;
import com.ghm.guesthousemanagementsystem.entity.*;
import com.ghm.guesthousemanagementsystem.enums.BookingStatus;
import com.ghm.guesthousemanagementsystem.exceptions.ResourceNotFoundException;
import com.ghm.guesthousemanagementsystem.helper.PricingCalculator;
import com.ghm.guesthousemanagementsystem.helper.ReferenceGenerator;
import com.ghm.guesthousemanagementsystem.helper.TokenGenerator;
import com.ghm.guesthousemanagementsystem.mapper.BookingMapper;
import com.ghm.guesthousemanagementsystem.mapper.PropertyMapper;
import com.ghm.guesthousemanagementsystem.mapper.RoomMapper;
import com.ghm.guesthousemanagementsystem.repository.*;
import com.ghm.guesthousemanagementsystem.service.BookingRoomService;
import com.ghm.guesthousemanagementsystem.service.BookingService;

import com.ghm.guesthousemanagementsystem.service.BookingStatusHistoryService;
import com.ghm.guesthousemanagementsystem.service.TemporaryTokenService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.Map;

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

    //Admin
    @Override
    @Transactional
    public BookingAdminResponseDto createBookingAsAdmin(BookingCreateRequestDto createDto) {

        List<UUID> roomIds = createDto.getRoomIds();
        List<Room> rooms = roomRepository.findAllById(roomIds);

        //Validate Property ID
        Property property = propertyRepository.findById(createDto.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        //Validate booking dates
        if(createDto.getCheckInDate().isAfter(createDto.getCheckOutDate())){
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        //Validate at least one room is selected
        if(rooms.isEmpty()) {
            throw new IllegalArgumentException("No valid rooms found for the provided id");
        }

        //Stop user from booking already booked rooms
        bookingRoomService.validateRoomAvailability(roomIds, createDto.getCheckInDate(), createDto.getCheckOutDate(), null);

        //Validate room counts
        if(rooms.size() != roomIds.size()) {
            throw new ResourceNotFoundException("One or more rooms not found");
        }

        //Validate total no. of guests fits room
        int totalCapacity = rooms.stream().mapToInt(Room::getMaxOccupancy).sum();

        if(createDto.getNoOfGuests() > totalCapacity) {
            throw new IllegalArgumentException("Selected no. of rooms cannot accommodate the number of guests");
        }

        //Map to Booking Entity
        Booking booking = BookingMapper.mapCreateDtoToBooking(createDto, property);

        String referenceId = ReferenceGenerator.generateReferenceId();
        BigDecimal totalPrice = PricingCalculator.calculateTotal(rooms, createDto.getCheckInDate(), createDto.getCheckOutDate());
        String token = TokenGenerator.generateToken();
        LocalDateTime now = LocalDateTime.now();

        //Setting data on entity
        booking.setReferenceId(referenceId);
        booking.setCreatedAt(now);
        booking.setUpdatedAt(now);
        booking.setStatus(BookingStatus.pending);
        booking.setTotalPrice(totalPrice);
        booking.setPaid(false);
        booking.setExpiredAt(now.plusDays(14));
        booking.setToken(token); //Should handle in TemporaryToken table...

        //Save entity
        bookingRepository.save(booking);

        //Setting a new status field in BookingRoom
        bookingRoomService.createBookingRooms(booking, rooms, createDto.getCheckInDate(), createDto.getCheckOutDate());

        //Setting a new status field in BookingStatusHistory
        bookingStatusHistoryService.statusChange(booking, null, BookingStatus.pending,null);

        //Setting a new token field in TemporaryTokenHistory
        temporaryTokenService.createTemporaryToken(booking, token, createDto.getCheckOutDate().plusMonths(1));



        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(property);

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms, createDto.getCheckInDate(), createDto.getCheckOutDate());

        return BookingMapper.mapBookingToAdminResponseDto(booking, propertySummaryDto, roomLineItemDtos);
    }

    @Override
    @Transactional
    public BookingAdminResponseDto updateBooking(UUID bookingId, BookingUpdateRequestDto updateDto) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new ResourceNotFoundException("Booking not found"));


        //List all requested rooms to attach
        List<Room> requestedNewRooms = roomRepository.findAllById(updateDto.getRoomIds());

        //Validate booking dates
        if(updateDto.getCheckInDate().isAfter(updateDto.getCheckOutDate())){
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        //Validate at least one room is selected
        if(requestedNewRooms.isEmpty()) {
            throw new IllegalArgumentException("No valid rooms found for the provided id");
        }

        if(requestedNewRooms.size() != updateDto.getRoomIds().size()){
            throw new ResourceNotFoundException("One or more rooms are not found");
        }


        //Validate room availability and clearing old booking room logs
        bookingRoomRepository.deleteAllByBooking(booking);

        bookingRoomService.validateRoomAvailability(
                requestedNewRooms.stream().map(Room::getId).toList(),
                updateDto.getCheckInDate(),
                updateDto.getCheckOutDate(),
                bookingId);

        //Recalculate total price
        BigDecimal totalPrice = PricingCalculator.calculateTotal(requestedNewRooms, updateDto.getCheckInDate(), updateDto.getCheckOutDate());

        //Validate total no. of guests fits room
        int totalCapacity = requestedNewRooms.stream().mapToInt(Room::getMaxOccupancy).sum();

        if(updateDto.getNoOfGuests() > totalCapacity) {
            throw new IllegalArgumentException("Selected no. of rooms cannot accommodate the number of guests");
        }

        //Map to Booking Entity
        BookingMapper.mapUpdateDtoToBooking(updateDto, booking);

        LocalDateTime now = LocalDateTime.now();

        //Setting data on entity
        booking.setUpdatedAt(now);
        booking.setTotalPrice(totalPrice);

        //Save entity
        bookingRepository.save(booking);

        //Attach all requested rooms
        List<BookingRoom> bookingRooms = requestedNewRooms.stream()
                .map(room -> new BookingRoom(booking, room, updateDto.getCheckInDate(), updateDto.getCheckOutDate()))
                .toList();
        bookingRoomRepository.saveAll(bookingRooms);

        //Setting a new status field in BookingStatusHistory
        bookingStatusHistoryService.statusChange(booking, BookingStatus.pending, BookingStatus.pending, "Admin updated booking");

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(requestedNewRooms, booking.getCheckInDate(), booking.getCheckOutDate());

        return BookingMapper.mapBookingToAdminResponseDto(booking, propertySummaryDto, roomLineItemDtos);
    }

    @Override
    @Transactional
    public BookingAdminResponseDto confirmBooking(UUID bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        List<BookingRoom> bookingRooms = bookingRoomRepository.findAllByBooking(booking);

        List<Room> rooms = bookingRooms.stream()
                .map(BookingRoom::getRoom)
                .toList();

        LocalDateTime now = LocalDateTime.now();

        booking.setStatus(BookingStatus.confirmed);
        booking.setPaid(true);
        booking.setConfirmedAt(now);

        bookingRepository.save(booking);

        //Setting a new status field in BookingStatusHistory
        bookingStatusHistoryService.statusChange(booking, BookingStatus.pending, BookingStatus.confirmed, null);

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms, booking.getCheckInDate(), booking.getCheckOutDate());

        return BookingMapper.mapBookingToAdminResponseDto(booking, propertySummaryDto, roomLineItemDtos);
    }

    @Override
    @Transactional
    public BookingAdminResponseDto cancelBookingAsAdmin(UUID bookingId, BookingCancelRequestDto cancelDto) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if(booking.getStatus() ==  BookingStatus.cancelled){
            throw new IllegalStateException("Booking is already cancelled");
        }

        List<Room> rooms = bookingRoomRepository.findAllByBooking_BookingId(bookingId)
                .stream()
                .map(BookingRoom::getRoom)
                .toList();

        LocalDateTime now = LocalDateTime.now();

        booking.setStatus(BookingStatus.cancelled);
        booking.setUpdatedAt(now);

        bookingRepository.save(booking);

        //Setting a new status field in BookingStatusHistory
        bookingStatusHistoryService.statusChange(booking, BookingStatus.pending, BookingStatus.cancelled,cancelDto.getReason());

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms, booking.getCheckInDate(), booking.getCheckOutDate());

        return BookingMapper.mapBookingToAdminResponseDto(booking,  propertySummaryDto, roomLineItemDtos);
    }

    @Override
    @Transactional
    public BookingAdminResponseDto attachRooms(UUID bookingId, BookingAttachRoomsRequestDto attachRoomsDto) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        //List already booked rooms
        List<Room> currentRooms = bookingRoomRepository.findAllByBooking_BookingId(bookingId)
                .stream()
                .map(BookingRoom::getRoom)
                .toList();

        //List all new rooms to attach
        List<Room> newRooms = roomRepository.findAllById(attachRoomsDto.getRoomIds());

        if(newRooms.size() != attachRoomsDto.getRoomIds().size()){
            throw new ResourceNotFoundException("One or more rooms are not found");
        }

        //Validate if there are rooms to attach
        List<Room> roomsToAttach = newRooms.stream()
                .filter(r -> !currentRooms.contains(r))
                .toList();
        if(roomsToAttach.isEmpty()){
            throw new IllegalArgumentException("No new rooms to attach");
        }

        //Validate room availability
        bookingRoomService.validateRoomAvailability(
                roomsToAttach.stream().map(Room::getId).toList(),
                booking.getCheckInDate(),
                booking.getCheckOutDate(),
                null);

        //Attach rooms
        List<BookingRoom> bookingRooms = roomsToAttach.stream()
                .map(room -> new BookingRoom(booking, room, booking.getCheckInDate(), booking.getCheckOutDate()))
                .toList();
        bookingRoomRepository.saveAll(bookingRooms);

        //Recalculate total price
        List<Room> totalNewRooms = Stream.concat(currentRooms.stream(), roomsToAttach.stream()).toList();

        BigDecimal totalPrice = PricingCalculator.calculateTotal(totalNewRooms, booking.getCheckInDate(), booking.getCheckOutDate());

        //Change fields and save booking
        booking.setTotalPrice(totalPrice);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(totalNewRooms, booking.getCheckInDate(), booking.getCheckOutDate());

        return BookingMapper.mapBookingToAdminResponseDto(booking, propertySummaryDto, roomLineItemDtos);
    }

    @Override
    public BookingAdminResponseDto getBookingById(UUID bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        List<Room> rooms = bookingRoomRepository.findAllByBooking_BookingId(bookingId).
                stream()
                .map(BookingRoom::getRoom)
                .toList();

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms, booking.getCheckInDate(), booking.getCheckOutDate());

        return BookingMapper.mapBookingToAdminResponseDto(booking, propertySummaryDto, roomLineItemDtos);
    }

    @Override
    public List<BookingAdminResponseDto> getAllBookingsAsAdmin() {

        List<Booking> bookings = bookingRepository.findAll();

        List<UUID> bookingIds = bookings.stream()
                .map(Booking::getBookingId)
                .toList();

        Map<UUID, List<Room>> roomsByBookingId = bookingRoomService.mapRoomsByBookingIds(bookingIds);

        return bookings.stream()
                .map(booking->{
                    PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());
                    List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(
                            roomsByBookingId.getOrDefault(booking.getBookingId(), List.of()),
                            booking.getCheckInDate(),
                            booking.getCheckOutDate()
                    );
                    return BookingMapper.mapBookingToAdminResponseDto(booking, propertySummaryDto, roomLineItemDtos);
                })
                .toList();
    }

    @Override
    public List<BookingSummaryResponseDto> getAllBookingSummariesAsAdmin() {
        List<Booking> bookings = bookingRepository.findAll();

        return bookings.stream()
                .map(booking-> BookingMapper.mapToSummaryResponseDto(booking))
                .toList();
    }


    //Guest
    @Override
    @Transactional
    public BookingGuestResponseDto createBookingAsGuest(BookingCreateRequestDto createDto) {

        List<UUID> roomIds = createDto.getRoomIds();
        List<Room> rooms = roomRepository.findAllById(roomIds);

        //Validate Property ID
        Property property = propertyRepository.findById(createDto.getPropertyId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        //Validate Booking Dates
        if(!createDto.getCheckInDate().isBefore(createDto.getCheckOutDate())){
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        //Validate at least one room is selected
        if(rooms.isEmpty()) {
            throw new IllegalArgumentException("No valid rooms found for the provided id");
        }

        //Stop user from booking already booked rooms
        bookingRoomService.validateRoomAvailability(roomIds, createDto.getCheckInDate(), createDto.getCheckOutDate(), null);

        //Validate Room counts
        if(rooms.size() != roomIds.size()) {
            throw new ResourceNotFoundException("One or more rooms not found");
        }

        //Validate total No. of guests fits room
        int totalCapacity = rooms.stream().mapToInt(Room::getMaxOccupancy).sum();

        if(createDto.getNoOfGuests() > totalCapacity) {
            throw new IllegalArgumentException("Selected no. of rooms cannot accommodate the number of guests");
        }

        //Map to Booking Entity
        Booking booking = BookingMapper.mapCreateDtoToBooking(createDto, property);

        String referenceId = ReferenceGenerator.generateReferenceId();
        BigDecimal totalPrice = PricingCalculator.calculateTotal(rooms, createDto.getCheckInDate(), createDto.getCheckOutDate());
        String token = TokenGenerator.generateToken();
        LocalDateTime now = LocalDateTime.now();

        booking.setReferenceId(referenceId);
        booking.setCreatedAt(now);
        booking.setUpdatedAt(now);
        booking.setStatus(BookingStatus.pending);
        booking.setTotalPrice(totalPrice);
        booking.setPaid(false);
        booking.setExpiredAt(now.plusDays(14));
        booking.setToken(token);

        bookingRepository.save(booking);

        //Setting a new status field in BookingRoom
        bookingRoomService.createBookingRooms(booking, rooms, createDto.getCheckInDate(), createDto.getCheckOutDate());

        //Setting a new status field in BookingStatusHistory
        bookingStatusHistoryService.statusChange(booking, null, BookingStatus.pending, null);

        //Setting a new token field in TemporaryTokenHistory
        temporaryTokenService.createTemporaryToken(booking, token, createDto.getCheckOutDate().plusMonths(1));

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(property);

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms, createDto.getCheckInDate(), createDto.getCheckOutDate());

        return BookingMapper.mapBookingToGuestResponseDto(booking, propertySummaryDto, roomLineItemDtos);
    }

    @Override
    @Transactional
    public BookingGuestResponseDto patchBooking(UUID bookingId, BookingPatchRequestDto patchDto) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new ResourceNotFoundException("Booking not found"));

        //Get all bookings made by guest during creation of booking
        List<Room> rooms = bookingRoomRepository.findAllByBooking_BookingId(bookingId)
                .stream()
                .map(BookingRoom::getRoom)
                .toList();

        //Validate total no. of guests fits room
        int totalCapacity = rooms.stream().mapToInt(Room::getMaxOccupancy).sum();

        if(patchDto.getNoOfGuests() != null){
            if(patchDto.getNoOfGuests() > totalCapacity) {
                throw new IllegalArgumentException("Selected no. of rooms cannot accommodate the number of guests");
            }
            booking.setNoOfGuests(patchDto.getNoOfGuests());
        }

        //Map to Booking Entity
        BookingMapper.mapPatchDtoToBooking(patchDto, booking);

        //Setting data on entity
        booking.setUpdatedAt(LocalDateTime.now());

        //Save entity
        bookingRepository.save(booking);

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms, booking.getCheckInDate(), booking.getCheckOutDate());
        return BookingMapper.mapBookingToGuestResponseDto(booking, propertySummaryDto, roomLineItemDtos);
    }

    @Override
    @Transactional      // Need to include room validation
    public BookingGuestResponseDto amendBooking(UUID bookingId, BookingAmendRequestDto amendDto) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new ResourceNotFoundException("Booking not found"));

        List<Room> rooms = bookingRoomRepository.findAllByBooking_BookingId(bookingId)
                .stream()
                .map(BookingRoom::getRoom)
                .toList();

        List<UUID> roomIds = rooms.stream()
                .map(Room::getId)
                .toList();

        //Validate booking dates
        if(amendDto.getNewCheckInDate().isAfter(amendDto.getNewCheckOutDate())){
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        //Check whether rooms are available for new time range
        bookingRoomService.validateRoomAvailability(roomIds,amendDto.getNewCheckInDate(), amendDto.getNewCheckOutDate(), bookingId);

        //Map to Booking Entity
        BookingMapper.mapAmendDtoToBooking(amendDto, booking);

        //Calculating total price
        BigDecimal totalPrice = PricingCalculator.calculateTotal(rooms, amendDto.getNewCheckInDate(), amendDto.getNewCheckOutDate());

        LocalDateTime now = LocalDateTime.now();

        //Setting data on entity
        booking.setUpdatedAt(now);
        booking.setTotalPrice(totalPrice);

        //Save entity
        bookingRepository.save(booking);

        //Setting a new status field in BookingStatusHistory
        bookingStatusHistoryService.statusChange(booking, BookingStatus.pending, BookingStatus.pending, "Guest amended the booking");

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms, amendDto.getNewCheckInDate(), amendDto.getNewCheckOutDate());

        return BookingMapper.mapBookingToGuestResponseDto(booking, propertySummaryDto, roomLineItemDtos);

    }

    @Override
    @Transactional
    public BookingGuestResponseDto cancelBookingAsGuest(UUID bookingId, BookingCancelRequestDto cancelDto) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if(booking.getStatus() ==  BookingStatus.cancelled){
            throw new IllegalStateException("Booking is already cancelled");
        }

        if(LocalDate.now().isAfter(booking.getCheckInDate())){
            throw new IllegalStateException("Guest cannot cancel after check-in date");
        }

        List<Room> rooms = bookingRoomRepository.findAllByBooking_BookingId(bookingId)
                .stream()
                .map(BookingRoom::getRoom)
                .toList();

        LocalDateTime now = LocalDateTime.now();

        //Setting a new status field in BookingStatusHistory
        bookingStatusHistoryService.statusChange(booking, BookingStatus.pending, BookingStatus.cancelled, cancelDto.getReason());

        booking.setStatus(BookingStatus.cancelled);
        booking.setUpdatedAt(now);
        bookingRepository.save(booking);

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms, booking.getCheckInDate(), booking.getCheckOutDate());

        return BookingMapper.mapBookingToGuestResponseDto(booking,  propertySummaryDto, roomLineItemDtos);
    }

    @Override
    public BookingGuestResponseDto getBookingByToken(String token)  {

        Booking booking = bookingRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        List<Room> rooms = bookingRoomRepository.findAllByBooking_BookingId(booking.getBookingId()).
                stream()
                .map(BookingRoom::getRoom)
                .toList();

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms,  booking.getCheckInDate(), booking.getCheckOutDate());

        return BookingMapper.mapBookingToGuestResponseDto(booking, propertySummaryDto, roomLineItemDtos);
    }

}
