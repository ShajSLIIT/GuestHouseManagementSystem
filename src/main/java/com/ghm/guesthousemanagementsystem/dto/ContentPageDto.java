package com.ghm.guesthousemanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentPageDto {

    private UUID id;
    private String slug;
    private String title;
    private String body;
    private LocalDateTime lastUpdated;

}
