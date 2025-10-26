package com.ghm.guesthousemanagementsystem.service;

import com.ghm.guesthousemanagementsystem.dto.PhotoCreateDto;
import com.ghm.guesthousemanagementsystem.dto.PhotoDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


public interface PhotoService {
    PhotoDto uploadPhoto(PhotoCreateDto photoCreateDto, MultipartFile file) throws IOException;
    PhotoDto getPhotoById(UUID photoId);
    List<PhotoDto> getPhotosByPropertyId(UUID propertyId);
    List<PhotoDto> getPhotosByRoomId(UUID roomId);
    PhotoDto updatePhoto(UUID photoId, String newCaption, MultipartFile newFile) throws IOException ;
    void deletePhoto(UUID photoId) throws IOException;
    void deletePhotosByRoom(UUID roomId);
    void deletePhotosByProperty(UUID propertyId);
}
