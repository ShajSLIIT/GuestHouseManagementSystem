package com.naveen.guesthousemanagementsystem.dto.review;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewResponseDTO {
    private String referenceId;
    private String guestName;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String propertyName;
}