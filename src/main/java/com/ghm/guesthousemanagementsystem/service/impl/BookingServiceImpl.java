package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.dto.booking.*;
import com.ghm.guesthousemanagementsystem.dto.supportingdto.PropertySummaryDto;
import com.ghm.guesthousemanagementsystem.dto.supportingdto.RoomLineItemDto;
import com.ghm.guesthousemanagementsystem.entity.Booking;
import com.ghm.guesthousemanagementsystem.entity.BookingRoom;
import com.ghm.guesthousemanagementsystem.entity.BookingStatusHistory;
import com.ghm.guesthousemanagementsystem.entity.Room;
import com.ghm.guesthousemanagementsystem.enums.BookingStatus;
import com.ghm.guesthousemanagementsystem.exceptions.ResourceNotFoundException;
import com.ghm.guesthousemanagementsystem.exceptions.RoomUnavailableException;
import com.ghm.guesthousemanagementsystem.helper.PricingCalculator;
import com.ghm.guesthousemanagementsystem.helper.ReferenceGenerator;
import com.ghm.guesthousemanagementsystem.helper.TokenGenerator;
import com.ghm.guesthousemanagementsystem.mapper.BookingMapper;
import com.ghm.guesthousemanagementsystem.repository.BookingRepository;
import com.ghm.guesthousemanagementsystem.repository.BookingRoomRepository;
import com.ghm.guesthousemanagementsystem.repository.BookingStatusHistoryRepository;
import com.ghm.guesthousemanagementsystem.service.BookingRoomService;
import com.ghm.guesthousemanagementsystem.service.BookingService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final RoomRepository roomRepository;
    private final BookingRoomRepository bookingRoomRepository;
    private final BookingStatusHistoryRepository bookingStatusHistoryRepository;

    private final BookingRoomService bookingRoomService;

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

        bookingRoomService.validateRoomAvailability(roomIds, createDto.getCheckInDate(), createDto.getCheckOutDate());

        //Validate room counts
        if(rooms.size() != roomIds.size()) {
            throw new ResourceNotFoundException("One or more rooms not found");
        }

        //Validate total no. of guests fits room
        int totalCapacity = rooms.stream().mapToInt(room -> room.getMaxOccupancy()).sum();

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

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(property);

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms);

        return BookingMapper.mapBookingToAdminResponseDto(booking, propertySummaryDto, roomLineItemDtos);
    }

    @Override
    @Transactional
    public BookingAdminResponseDto updateBooking(UUID bookingId, BookingUpdateRequestDto updateDto) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new ResourceNotFoundException("Booking not found"));

        List<UUID> roomIds = updateDto.getRoomIds();
        List<Room> rooms = roomRepository.findAllById(roomIds);

        //Validate booking dates
        if(updateDto.getCheckInDate().isAfter(updateDto.getCheckOutDate())){
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        //Validate at least one room is selected
        if(rooms.isEmpty()) {
            throw new IllegalArgumentException("No valid rooms found for the provided id");
        }

        //Stop user from booking already booked rooms
        bookingRoomService.validateRoomAvailability(roomIds, updateDto.getCheckInDate(), updateDto.getCheckOutDate());

        //Validate room counts
        if(rooms.size() != roomIds.size()) {
            throw new ResourceNotFoundException("One or more rooms not found");
        }

        //Validate total no. of guests fits room
        int totalCapacity = rooms.stream().mapToInt(room -> room.getMaxOccupancy()).sum();

        if(updateDto.getNoOfGuests() > totalCapacity) {
            throw new IllegalArgumentException("Selected no. of rooms cannot accommodate the number of guests");
        }

        //Map to Booking Entity
        BookingMapper.mapUpdateDtoToBooking(updateDto, booking);

        //Calculating total price
        BigDecimal totalPrice = PricingCalculator.calculateTotal(rooms, updateDto.getCheckInDate(), updateDto.getCheckOutDate());

        //Setting data on entity
        booking.setUpdatedAt(LocalDateTime.now());
        booking.setTotalPrice(totalPrice);

        //Save entity
        bookingRepository.save(booking);

        //Updating BookingRoom Table
        bookingRoomRepository.deleteByBooking(booking);     //Future implement: Without deleting all and add again we can only add new rooms
        List<BookingRoom> bookingRooms = rooms.stream()
                .map(room -> new BookingRoom(booking, room, updateDto.getCheckInDate(), updateDto.getCheckOutDate()))
                .toList();
        bookingRoomRepository.saveAll(bookingRooms);

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms);

        return BookingMapper.mapBookingToAdminResponseDto(booking, propertySummaryDto, roomLineItemDtos);
    }

    @Override
    @Transactional
    public BookingAdminResponseDto confirmBooking(UUID bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        List<BookingRoom> bookingRooms = bookingRoomRepository.findAllByBooking(booking);

        List<Room> rooms = bookingRooms.stream()
                .map(bookingRoom -> bookingRoom.getRoom())
                .toList();

        booking.setStatus(BookingStatus.confirmed);
        booking.setPaid(true);

        bookingRepository.save(booking);

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms);

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
                .map(bookingRoom -> bookingRoom.getRoom())
                .toList();

        //Setting a new status field in BookingStatusHistory
        BookingStatusHistory statusHistory = new BookingStatusHistory();
        statusHistory.setBooking(booking);
        statusHistory.setFromStatus(booking.getStatus());
        statusHistory.setToStatus(BookingStatus.cancelled);
        statusHistory.setChangedAt(LocalDateTime.now());
        statusHistory.setReason(cancelDto.getReason());

        bookingStatusHistoryRepository.save(statusHistory);

        booking.setStatus(BookingStatus.cancelled);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms);

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
                .map(bookingRoom -> bookingRoom.getRoom())
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
                roomsToAttach.stream().map(room -> room.getId()).toList(),
                booking.getCheckInDate(),
                booking.getCheckOutDate());

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
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(totalNewRooms);

        return BookingMapper.mapBookingToAdminResponseDto(booking, propertySummaryDto, roomLineItemDtos);
    }

    @Override
    public BookingAdminResponseDto getBookingById(UUID bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        List<Room> rooms = bookingRoomRepository.findAllByBooking_BookingId(bookingId).
                stream()
                .map(bookingRoom -> bookingRoom.getRoom())
                .toList();

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms);

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
                            roomsByBookingId.getOrDefault(booking.getBookingId(), List.of())
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
        bookingRoomService.validateRoomAvailability(roomIds, createDto.getCheckInDate(), createDto.getCheckOutDate());

        //Validate Room counts
        if(rooms.size() != roomIds.size()) {
            throw new ResourceNotFoundException("One or more rooms not found");
        }

        //Validate total No. of guests fits room
        int totalCapacity = rooms.stream().mapToInt(room -> room.getMaxOccupancy()).sum();

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

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(property);

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms);

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
                .map(bookingRoom -> bookingRoom.getRoom())
                .toList();

                //Validate total no. of guests fits room
        int totalCapacity = rooms.stream().mapToInt(room -> room.getMaxOccupancy()).sum();

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
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms);

        return BookingMapper.mapBookingToGuestResponseDto(booking, propertySummaryDto, roomLineItemDtos);
    }

    @Override
    @Transactional      // Need to include room validation
    public BookingGuestResponseDto amendBooking(UUID bookingId, BookingAmendRequestDto amendDto) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new ResourceNotFoundException("Booking not found"));

        List<Room> rooms = bookingRoomRepository.findAllByBooking_BookingId(bookingId)
                .stream()
                .map(bookingRoom -> bookingRoom.getRoom())
                .toList();

        //Validate booking dates
        if(amendDto.getNewCheckInDate().isAfter(amendDto.getNewCheckOutDate())){
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        //Map to Booking Entity
        BookingMapper.mapAmendDtoToBooking(amendDto, booking);

        //Calculating total price
        BigDecimal totalPrice = PricingCalculator.calculateTotal(rooms, amendDto.getNewCheckInDate(), amendDto.getNewCheckOutDate());

        //Setting data on entity
        booking.setUpdatedAt(LocalDateTime.now());
        booking.setTotalPrice(totalPrice);

        //Save entity
        bookingRepository.save(booking);

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms);

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
                .map(bookingRoom -> bookingRoom.getRoom())
                .toList();

        //Setting a new status field in BookingStatusHistory
        BookingStatusHistory statusHistory = new BookingStatusHistory();
        statusHistory.setBooking(booking);
        statusHistory.setFromStatus(booking.getStatus());
        statusHistory.setToStatus(BookingStatus.cancelled);
        statusHistory.setChangedAt(LocalDateTime.now());
        statusHistory.setReason(cancelDto.getReason());

        bookingStatusHistoryRepository.save(statusHistory);

        booking.setStatus(BookingStatus.cancelled);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.save(booking);

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms);

        return BookingMapper.mapBookingToGuestResponseDto(booking,  propertySummaryDto, roomLineItemDtos);
    }

    @Override
    public BookingGuestResponseDto getBookingByToken(String token)  {

        Booking booking = bookingRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        List<Room> rooms = bookingRoomRepository.findAllByBooking_BookingId(booking.getBookingId()).
                stream()
                .map(bookingRoom -> bookingRoom.getRoom())
                .toList();

        //Map to PropertySummaryDto
        PropertySummaryDto propertySummaryDto = PropertyMapper.mapPropertyToPropertySummaryDto(booking.getProperty());

        //Map to RoomLineItemDto
        List<RoomLineItemDto> roomLineItemDtos = RoomMapper.mapRoomToRoomLineDto(rooms);

        return BookingMapper.mapBookingToGuestResponseDto(booking, propertySummaryDto, roomLineItemDtos);
    }
    
}
