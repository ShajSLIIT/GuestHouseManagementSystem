package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.dto.PropertyCreateDto;
import com.ghm.guesthousemanagementsystem.dto.PropertyDetailsDto;
import com.ghm.guesthousemanagementsystem.dto.PropertySummaryDto;
import com.ghm.guesthousemanagementsystem.dto.PropertyUpdateDto;
import com.ghm.guesthousemanagementsystem.entity.Property;
import com.ghm.guesthousemanagementsystem.entity.AdminUser;
import com.ghm.guesthousemanagementsystem.entity.Room;
import com.ghm.guesthousemanagementsystem.entity.BookingRoom;
import com.ghm.guesthousemanagementsystem.exception.ResourceNotFoundException;
import com.ghm.guesthousemanagementsystem.mapper.PropertyMapper;
import com.ghm.guesthousemanagementsystem.repository.PropertyRepository;
import com.ghm.guesthousemanagementsystem.repository.AdminUserRepository;
import com.ghm.guesthousemanagementsystem.repository.RoomRepository;
import com.ghm.guesthousemanagementsystem.repository.BookingRoomRepository;
import com.ghm.guesthousemanagementsystem.service.PropertyService;
import com.ghm.guesthousemanagementsystem.service.PhotoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final PhotoService photoService;
    private final AdminUserRepository adminUserRepository;
    private final RoomRepository roomRepository;
    private final BookingRoomRepository bookingRoomRepository;

    //  Returns a lightweight summary dtos
    @Override
    public List<PropertySummaryDto> getAllProperties(){
        List<Property> properties = propertyRepository.findAll();
        return properties.stream()
                .map(PropertyMapper::mapToPropertyDto)
                .collect(Collectors.toList());
    }

    //  Full detail + rooms
    // Can also put @Transactional for this but since im using @EntityGraph in the repo, this is the supposed way
    @Override
    public PropertyDetailsDto getPropertyDetailById(UUID id) {
        Property property = propertyRepository.findWithRoomsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        PropertyDetailsDto detailsDto = PropertyMapper.mapToPropertyDetailsDto(property);

        // Fetch and add photos
        try {
            List<String> photoUrls = photoService.getPhotosByPropertyId(id)
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

    //  New properties don't carry ID, and you only return a summary view back
    @Override
    public PropertySummaryDto createProperty(PropertyCreateDto propertyDto, String adminEmail) {
        // Find the admin user by email
        AdminUser adminUser = adminUserRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found with email: " + adminEmail));

        Property property = PropertyMapper.mapToNewProperty(propertyDto);
        property.setManager(adminUser); // Set the manager
        Property saved = propertyRepository.save(property);
        return PropertyMapper.mapToPropertyDto(saved);
    }


    //  Allows partial updates, using PropertyUpdateDto
    @Override
    public PropertySummaryDto updateProperty(UUID id, PropertyUpdateDto dto) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + id));
        PropertyMapper.updatePropertyFromDto(dto, property);
        Property updated = propertyRepository.save(property);

        return PropertyMapper.mapToPropertyDto(updated);
    }

    @Override
    public PropertyDetailsDto updatePropertyDetail(UUID id, PropertyUpdateDto dto){
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + id));
        PropertyMapper.updatePropertyFromDto(dto, property);
        Property updated = propertyRepository.save(property);

        PropertyDetailsDto detailsDto = PropertyMapper.mapToPropertyDetailsDto(updated);

        // Fetch and add photos
        try {
            List<String> photoUrls = photoService.getPhotosByPropertyId(id)
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
    public void deleteProperty(UUID id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + id));

        // Get all rooms for this property using repository (to avoid lazy initialization)
        List<Room> rooms = roomRepository.findByPropertyId(id);

        // Delete all rooms first (which will handle their photos and booking_room records)
        for (Room room : rooms) {
            // Delete all booking room records first
            List<BookingRoom> bookingRooms = bookingRoomRepository.findAllByRoom(room);
            bookingRoomRepository.deleteAll(bookingRooms);

            // Delete all photos associated with this room
            photoService.deletePhotosByRoom(room.getId());

            // Delete the room
            roomRepository.delete(room);
        }

        // Delete all property photos
        photoService.deletePhotosByProperty(id);

        // Finally delete the property
        propertyRepository.delete(property);
    }



}
