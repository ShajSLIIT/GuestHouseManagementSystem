package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.dto.PhotoCreateDto;
import com.ghm.guesthousemanagementsystem.dto.PhotoDto;
import com.ghm.guesthousemanagementsystem.entity.Photo;
import com.ghm.guesthousemanagementsystem.entity.Property;
import com.ghm.guesthousemanagementsystem.entity.Room;
import com.ghm.guesthousemanagementsystem.exception.ResourceNotFoundException;
import com.ghm.guesthousemanagementsystem.repository.PhotoRepo;
import com.ghm.guesthousemanagementsystem.repository.PropertyRepository;
import com.ghm.guesthousemanagementsystem.repository.RoomRepository;
import com.ghm.guesthousemanagementsystem.service.PhotoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;


@Service
@AllArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private PropertyRepository propertyRepository;
    private RoomRepository roomRepository;
    private PhotoRepo photoRepository;

    @Override
    public PhotoDto uploadPhoto(PhotoCreateDto photoData, MultipartFile file) throws IOException {
//        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//        String url = (String) uploadResult.get("secure_url");

        String uploadDir = "uploads/photo/";
        File dir = new File(uploadDir);
        if (!dir.exists()){
            dir.mkdirs();   // create the folder if missing
        }

        // Save file
        String filename = String.valueOf(UUID.randomUUID()) + "_" + file.getOriginalFilename();
        Path filepath = Paths.get(uploadDir, filename);
        Files.copy(file.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println(filename);
        String url = "http://localhost:8080/uploads/photo/" + filename;

        // Create photo entity
        Photo photo = new Photo();
        photo.setImageUrl(url);
        photo.setCaption(photoData.getCaption());

        if(photoData.getPropertyId() != null){
            Property property = propertyRepository.findById(photoData.getPropertyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Property not found"));
            photo.setProperty(property);
        }
        else if(photoData.getRoomId() != null){
            Room room = roomRepository.findById(photoData.getRoomId())
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
            photo.setRoom(room);
        }

        photo = photoRepository.save(photo);

        return new PhotoDto(photo.getId(), photo.getImageUrl(), photo.getCaption());

    }

    @Override
    public PhotoDto getPhotoById(UUID photoId) {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found"));
        return new PhotoDto(photo.getId(), photo.getImageUrl(), photo.getCaption());
    }

    @Override
    public List<PhotoDto> getPhotosByPropertyId(UUID propertyId) {
        List<Photo> photos = photoRepository.findByPropertyId(propertyId);
        return photos.stream()
                .map(photo -> new PhotoDto(photo.getId(), photo.getImageUrl(), photo.getCaption()))
                .toList();
    }

    @Override
    public List<PhotoDto> getPhotosByRoomId(UUID roomId) {
        List<Photo> photos = photoRepository.findByRoomId(roomId);
        return photos.stream()
                .map(photo -> new PhotoDto(photo.getId(), photo.getImageUrl(), photo.getCaption()))
                .toList();
    }

    @Override
    public PhotoDto updatePhoto(UUID photoId, String newCaption, MultipartFile newFile) throws IOException {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found"));

        // Update caption if provided
        if (newCaption != null && !newCaption.trim().isEmpty()) {
            photo.setCaption(newCaption);
        }

        // Replace file if new file is provided
        if (newFile != null && !newFile.isEmpty()) {
            // Delete the old file (optional but recommended)
            String oldUrl = photo.getImageUrl();
            if (oldUrl != null && oldUrl.startsWith("http://localhost:8080/uploads/photo/")) {
                String filename = oldUrl.substring(oldUrl.lastIndexOf('/') + 1);
                File oldFile = new File("uploads/photo/" + filename);
                if (oldFile.exists()) oldFile.delete();
            }

            // Save the new file
            String uploadDir = "uploads/photo/";
            File dir = new File(uploadDir);
            if (!dir.exists()) dir.mkdirs();

            String filename = UUID.randomUUID() + "_" + newFile.getOriginalFilename();
            Path filepath = Paths.get(uploadDir, filename);
            Files.copy(newFile.getInputStream(), filepath, StandardCopyOption.REPLACE_EXISTING);

            // Update the photo's URL
            String newUrl = "http://localhost:8080/uploads/photo/" + filename;
            photo.setImageUrl(newUrl);
        }

        // Save updates
        photo = photoRepository.save(photo);

        // Return updated PhotoDto
        // can also use a mapper
        return new PhotoDto(photo.getId(), photo.getImageUrl(), photo.getCaption());
    }


    @Override
    public void deletePhoto(UUID photoId) throws IOException {
        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found"));

        // Optionally delete from Cloudinary too using public_id (if stored)
        photoRepository.delete(photo);
    }

    @Override
    public void deletePhotosByRoom(UUID roomId) {
        List<Photo> photos = photoRepository.findByRoomId(roomId);
        photoRepository.deleteAll(photos);
    }

    @Override
    public void deletePhotosByProperty(UUID propertyId) {
        List<Photo> photos = photoRepository.findByPropertyId(propertyId);
        photoRepository.deleteAll(photos);
    }
}
