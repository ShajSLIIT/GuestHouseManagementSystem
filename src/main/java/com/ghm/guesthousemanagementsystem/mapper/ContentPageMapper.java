package com.ghm.guesthousemanagementsystem.mapper;

import com.ghm.guesthousemanagementsystem.dto.ContentPageDto;
import com.ghm.guesthousemanagementsystem.entity.ContentPage;

public class ContentPageMapper {

    public static ContentPageDto toDto(ContentPage page) {

        return new ContentPageDto(
                page.getId(),
                page.getSlug(),
                page.getTitle(),
                page.getBody(),
                page.getLastUpdated()
        );
    }

    public static ContentPage toEntity(ContentPageDto dto) {
        return new ContentPage(
                dto.getId(),
                dto.getSlug(),
                dto.getTitle(),
                dto.getBody(),
                dto.getLastUpdated()
        );
    }
}
