package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.dto.RoomCreateDto;
import com.ghm.guesthousemanagementsystem.dto.RoomDetailsDto;
import com.ghm.guesthousemanagementsystem.dto.RoomDto;
import com.ghm.guesthousemanagementsystem.dto.RoomUpdateDto;
import com.ghm.guesthousemanagementsystem.entity.Property;
import com.ghm.guesthousemanagementsystem.entity.Room;
import com.ghm.guesthousemanagementsystem.entity.BookingRoom;
import com.ghm.guesthousemanagementsystem.exception.ResourceNotFoundException;
import com.ghm.guesthousemanagementsystem.exception.UnauthorizedAccessException;
import com.ghm.guesthousemanagementsystem.mapper.RoomMapper;
import com.ghm.guesthousemanagementsystem.repository.PropertyRepository;
import com.ghm.guesthousemanagementsystem.repository.RoomRepository;
import com.ghm.guesthousemanagementsystem.repository.BookingRoomRepository;
import com.ghm.guesthousemanagementsystem.service.RoomService;
import com.ghm.guesthousemanagementsystem.service.PhotoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final PropertyRepository propertyRepository;
    private final PhotoService photoService;
    private final BookingRoomRepository bookingRoomRepository;

    @Override
    public RoomDetailsDto createRoom(RoomCreateDto dto, UUID propertyId) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found"));

        Room room = RoomMapper.mapToNewRoom(dto, property);
        Room saved = roomRepository.save(room);
        property.addTotalRooms();
        RoomDetailsDto detailsDto = RoomMapper.mapToRoomDetailsDto(saved);

        // Fetch and add photos
        try {
            List<String> photoUrls = photoService.getPhotosByRoomId(saved.getId())
                    .stream()
                    .map(photo -> photo.getUrl())
                    .collect(Collectors.toList());
            detailsDto.setPhotoUrls(photoUrls);
        } catch (Exception e) {
            // If photo fetching fails, set empty list
            detailsDto.setPhotoUrls(List.of());
        }

        return detailsDto;
    }
    @Override
    public List<RoomDto> getAllRoomsByPropertyId(UUID propertyId) {
        List<Room> rooms = roomRepository.findByPropertyId(propertyId);
        return rooms.stream()
                .map(RoomMapper::mapToRoomDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoomDetailsDto getRoomDetailsById(UUID id) {
        Room room = roomRepository.findDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id " + id));

        RoomDetailsDto detailsDto = RoomMapper.mapToRoomDetailsDto(room);

        // Fetch and add photos
        try {
            List<String> photoUrls = photoService.getPhotosByRoomId(id)
                    .stream()
                    .map(photo -> photo.getUrl())
                    .collect(Collectors.toList());
            detailsDto.setPhotoUrls(photoUrls);
        } catch (Exception e) {
            // If photo fetching fails, set empty list
            detailsDto.setPhotoUrls(List.of());
        }

        return detailsDto;
    }

    @Override
    public RoomDetailsDto updateRoomDetail(UUID id, RoomUpdateDto dto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + id));
        RoomMapper.updateRoomFromDto(dto, room);
        Room updated = roomRepository.save(room);

        // Refetch the room with details to avoid lazy loading issues
        Room roomWithDetails = roomRepository.findDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        RoomDetailsDto detailsDto = RoomMapper.mapToRoomDetailsDto(roomWithDetails);

        // Fetch and add photos
        try {
            List<String> photoUrls = photoService.getPhotosByRoomId(id)
                    .stream()
                    .map(photo -> photo.getUrl())
                    .collect(Collectors.toList());
            detailsDto.setPhotoUrls(photoUrls);
        } catch (Exception e) {
            // If photo fetching fails, set empty list
            detailsDto.setPhotoUrls(List.of());
        }

        return detailsDto;
    }

    @Override
    public RoomDetailsDto updateRoom(UUID id, RoomUpdateDto dto) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        RoomMapper.updateRoomFromDto(dto, room);
        Room updated = roomRepository.save(room);

        // Refetch the room with details to avoid lazy loading issues
        Room roomWithDetails = roomRepository.findDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        RoomDetailsDto detailsDto = RoomMapper.mapToRoomDetailsDto(roomWithDetails);

        // Fetch and add photos
        try {
            List<String> photoUrls = photoService.getPhotosByRoomId(id)
                    .stream()
                    .map(photo -> photo.getUrl())
                    .collect(Collectors.toList());
            detailsDto.setPhotoUrls(photoUrls);
        } catch (Exception e) {
            // If photo fetching fails, set empty list
            detailsDto.setPhotoUrls(List.of());
        }

        return detailsDto;
    }

    @Override
    public RoomDetailsDto showRoom(UUID id, RoomUpdateDto dto) {
        Room room = roomRepository.findDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        RoomMapper.showRoomFromDto(dto, room);
        Room updated = roomRepository.save(room);

        // Refetch the room with details to avoid lazy loading issues
        Room roomWithDetails = roomRepository.findDetailsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        RoomDetailsDto detailsDto = RoomMapper.mapToRoomDetailsDto(roomWithDetails);

        // Fetch and add photos
        try {
            List<String> photoUrls = photoService.getPhotosByRoomId(id)
                    .stream()
                    .map(photo -> photo.getUrl())
                    .collect(Collectors.toList());
            detailsDto.setPhotoUrls(photoUrls);
        } catch (Exception e) {
            // If photo fetching fails, set empty list
            detailsDto.setPhotoUrls(List.of());
        }

        return detailsDto;
    }
    @Override
    public void deleteRoom(UUID id, Property property){
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        if (!room.getProperty().getId().equals(property.getId())) {
            throw new UnauthorizedAccessException("You do not have permission to delete this room.");
        }

        // Delete all booking room records first to avoid foreign key constraint
        List<BookingRoom> bookingRooms = bookingRoomRepository.findAllByRoom(room);
        bookingRoomRepository.deleteAll(bookingRooms);

        // Delete all photos associated with this room
        photoService.deletePhotosByRoom(room.getId());

        roomRepository.delete(room);
    }

    @Override
    public void deleteRoom(UUID id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));

        // Delete all booking room records first to avoid foreign key constraint
        List<BookingRoom> bookingRooms = bookingRoomRepository.findAllByRoom(room);
        bookingRoomRepository.deleteAll(bookingRooms);

        // Delete all photos associated with this room
        photoService.deletePhotosByRoom(room.getId());

        roomRepository.delete(room);
    }
}
