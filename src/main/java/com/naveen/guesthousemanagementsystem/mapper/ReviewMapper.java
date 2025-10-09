package com.naveen.guesthousemanagementsystem.mapper;

import com.naveen.guesthousemanagementsystem.dto.review.ReviewRequestDTO;
import com.naveen.guesthousemanagementsystem.dto.review.ReviewResponseDTO;
import com.naveen.guesthousemanagementsystem.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    /** Convert ReviewRequestDTO to Review entity */
    public Review toEntity(ReviewRequestDTO requestDTO) {
        if (requestDTO == null) return null;

        return Review.builder()
                .bookingId(requestDTO.getBookingId())
                .rating(requestDTO.getRating())
                .comment(requestDTO.getComment())
                .build();
    }

    /** Convert Review entity to ReviewResponseDTO */
    public ReviewResponseDTO toResponseDTO(Review review) {
        if (review == null) return null;

        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setId(review.getId());
        dto.setBookingId(review.getBookingId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        return dto;
    }

    /** Update Review entity from ReviewRequestDTO */
    public void updateEntityFromDTO(ReviewRequestDTO requestDTO, Review review) {
        if (requestDTO == null || review == null) return;

        if (requestDTO.getRating() != null) {
            review.setRating(requestDTO.getRating());
        }
        review.setComment(requestDTO.getComment());
    }
}