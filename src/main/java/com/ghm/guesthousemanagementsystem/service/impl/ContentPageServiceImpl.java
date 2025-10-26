package com.ghm.guesthousemanagementsystem.service.impl;

import com.ghm.guesthousemanagementsystem.dto.ContentPageDto;
import com.ghm.guesthousemanagementsystem.entity.ContentPage;
import com.ghm.guesthousemanagementsystem.exception.ResourceNotFoundException;
import com.ghm.guesthousemanagementsystem.mapper.ContentPageMapper;
import com.ghm.guesthousemanagementsystem.repository.ContentPageRepository;
import com.ghm.guesthousemanagementsystem.service.ContentPageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ContentPageServiceImpl implements ContentPageService {
    private final ContentPageRepository pageRepo;

    @Override
    public ContentPageDto createContentPage(ContentPageDto pageDto) {
        ContentPage page = ContentPageMapper.toEntity(pageDto);
        page.setLastUpdated(LocalDateTime.now());
        return ContentPageMapper.toDto(pageRepo.save(page));
    }

    @Override
    public List<ContentPageDto> getAllContentPages() {
        return pageRepo.findAll().stream().map(ContentPageMapper::toDto).toList();
    }

    @Override
    public ContentPageDto getContentPageById(UUID id) {
        ContentPage page = pageRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContentPage not found with id " + id));
        return ContentPageMapper.toDto(page);
    }

    @Override
    public ContentPageDto updateContentPage(UUID id, ContentPageDto pageDto) {
        ContentPage existing = pageRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContentPage not found with id " + id));

        existing.setSlug(pageDto.getSlug());
        existing.setTitle(pageDto.getTitle());
        existing.setBody(pageDto.getBody());
        existing.setLastUpdated(LocalDateTime.now());

        return ContentPageMapper.toDto(pageRepo.save(existing));
    }

    @Override
    public void deleteContentPage(UUID id) {
        ContentPage existing = pageRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContentPage not found with id " + id));
        pageRepo.delete(existing);
    }
}
