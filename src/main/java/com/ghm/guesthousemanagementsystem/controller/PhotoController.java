package com.ghm.guesthousemanagementsystem.controller;

import com.ghm.guesthousemanagementsystem.dto.PhotoCreateDto;
import com.ghm.guesthousemanagementsystem.dto.PhotoDto;
import com.ghm.guesthousemanagementsystem.service.PhotoService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("/upload")
    public ResponseEntity<PhotoDto> uploadPhoto(@ModelAttribute PhotoCreateDto photoData, @RequestParam("file") MultipartFile file) throws IOException
    {
        PhotoDto uploaded = photoService.uploadPhoto(photoData,file);
        return ResponseEntity.ok(uploaded);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoDto> getPhotoById(@PathVariable UUID id){
        PhotoDto photo = photoService.getPhotoById(id);
        return ResponseEntity.ok(photo);
    }

    @GetMapping("property/{propertyId}")
    public ResponseEntity<List<PhotoDto>> getPhotosByProperty(@PathVariable UUID propertyId) {
        List<PhotoDto> photos = photoService.getPhotosByPropertyId(propertyId);
        return ResponseEntity.ok(photos);
    }

    @GetMapping("property/{roomId}")
    public ResponseEntity<List<PhotoDto>> getPhotosByRoom(@PathVariable UUID roomId) {
        List<PhotoDto> photos = photoService.getPhotosByRoomId(roomId);
        return ResponseEntity.ok(photos);
    }
}
