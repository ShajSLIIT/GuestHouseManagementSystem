package com.ghm.guesthousemanagementsystem.service;

import com.ghm.guesthousemanagementsystem.dto.ContentPageDto;
import com.ghm.guesthousemanagementsystem.entity.ContentPage;

import java.util.List;
import java.util.UUID;

public interface ContentPageService {

    ContentPageDto createContentPage(ContentPageDto pageDto);
    List<ContentPageDto> getAllContentPages();
    ContentPageDto getContentPageById(UUID id);
    ContentPageDto updateContentPage(UUID id, ContentPageDto pageDto);
    void deleteContentPage(UUID id);

}
