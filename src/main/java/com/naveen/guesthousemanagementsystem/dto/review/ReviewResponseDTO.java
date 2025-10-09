package com.naveen.guesthousemanagementsystem.dto.review;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ReviewResponseDTO {
    private UUID id;
    private Long bookingId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}