package com.naveen.guesthousemanagementsystem.controller;

import com.naveen.guesthousemanagementsystem.dto.review.ReviewRequestDTO;
import com.naveen.guesthousemanagementsystem.dto.review.ReviewResponseDTO;
import com.naveen.guesthousemanagementsystem.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /** Get all reviews */
    @GetMapping
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    /** Get review by ID */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable UUID id) {
        return ResponseEntity.ok(reviewService.getReviewById(id));
    }

    /** Get reviews by booking ID */
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(reviewService.getReviewsByBooking(bookingId));
    }

    /** Create new review */
    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(@Valid @RequestBody ReviewRequestDTO request) {
        ReviewResponseDTO response = reviewService.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /** Update existing review */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponseDTO> updateReview(
            @PathVariable UUID id,
            @Valid @RequestBody ReviewRequestDTO request) {
        ReviewResponseDTO response = reviewService.updateReview(id, request);
        return ResponseEntity.ok(response);
    }

    /** Delete review */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable UUID id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok().build();
    }
}