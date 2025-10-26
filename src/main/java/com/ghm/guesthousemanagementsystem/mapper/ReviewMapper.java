package com.ghm.guesthousemanagementsystem.mapper;


import com.naveen.guesthousemanagementsystem.dto.review.ReviewRequestDTO;
import com.naveen.guesthousemanagementsystem.dto.review.ReviewResponseDTO;
import com.naveen.guesthousemanagementsystem.entity.Booking;
import com.naveen.guesthousemanagementsystem.entity.Review;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReviewMapper {

    public Review toEntity(ReviewRequestDTO requestDTO, Booking booking) {
        if (requestDTO == null || booking == null) return null;

        return Review.builder()
                .booking(booking)
                .rating(requestDTO.getRating())
                .comment(requestDTO.getComment())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public ReviewResponseDTO toResponseDTO(Review review) {
        if (review == null) return null;

        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());

        if (review.getBooking() != null) {
            dto.setReferenceId(review.getBooking().getReferenceId());
            dto.setGuestName(review.getBooking().getGuestName());
            if (review.getBooking().getProperty() != null) {
                dto.setPropertyName(review.getBooking().getProperty().getName());
            }
        }

        return dto;
    }

    public void updateEntityFromDTO(ReviewRequestDTO requestDTO, Review review) {
        if (requestDTO == null || review == null) return;

        if (requestDTO.getRating() != null) {
            review.setRating(requestDTO.getRating());
        }
        if (requestDTO.getComment() != null) {
            review.setComment(requestDTO.getComment());
        }
        review.setUpdatedAt(LocalDateTime.now());
    }
}