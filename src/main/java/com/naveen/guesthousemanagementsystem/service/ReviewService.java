package com.naveen.guesthousemanagementsystem.service;


import com.naveen.guesthousemanagementsystem.dto.review.ReviewRequestDTO;
import com.naveen.guesthousemanagementsystem.dto.review.ReviewResponseDTO;
import com.naveen.guesthousemanagementsystem.entity.Review;
import com.naveen.guesthousemanagementsystem.mapper.ReviewMapper;
import com.naveen.guesthousemanagementsystem.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    /** Get all reviews */
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /** Get reviews for specific booking */
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getReviewsByBooking(Long bookingId) {
        return reviewRepository.findByBookingId(bookingId).stream()
                .map(reviewMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /** Get review by ID */
    @Transactional(readOnly = true)
    public ReviewResponseDTO getReviewById(UUID id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
        return reviewMapper.toResponseDTO(review);
    }

    /** Create new review */
    public ReviewResponseDTO createReview(ReviewRequestDTO request) {
        if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1-5");
        }
        if (reviewRepository.existsByBookingId(request.getBookingId())) {
            throw new IllegalArgumentException("Review already exists for this booking");
        }

        Review review = reviewMapper.toEntity(request);
        Review saved = reviewRepository.save(review);
        return reviewMapper.toResponseDTO(saved);
    }

    /** Update existing review */
    public ReviewResponseDTO updateReview(UUID id, ReviewRequestDTO request) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));

        if (request.getRating() != null && (request.getRating() < 1 || request.getRating() > 5)) {
            throw new IllegalArgumentException("Rating must be between 1-5");
        }

        reviewMapper.updateEntityFromDTO(request, existingReview);
        Review updated = reviewRepository.save(existingReview);
        return reviewMapper.toResponseDTO(updated);
    }

    /** Delete review */
    public void deleteReview(UUID id) {
        if (!reviewRepository.existsById(id)) {
            throw new RuntimeException("Review not found with id: " + id);
        }
        reviewRepository.deleteById(id);
    }
}