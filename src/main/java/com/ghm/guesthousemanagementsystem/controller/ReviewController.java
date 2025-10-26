package com.ghm.guesthousemanagementsystem.controller;

import com.ghm.guesthousemanagementsystem.dto.review.ReviewRequestDTO;
import com.ghm.guesthousemanagementsystem.dto.review.ReviewResponseDTO;
import com.ghm.guesthousemanagementsystem.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // ADMIN ENDPOINTS

    @GetMapping("/admin")
    public ResponseEntity<List<ReviewResponseDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/admin/reference/{referenceId}")
    public ResponseEntity<ReviewResponseDTO> getReviewByReferenceId(@PathVariable String referenceId) {
        return ResponseEntity.ok(reviewService.getReviewByReferenceId(referenceId));
    }

    @GetMapping("/admin/property/{propertyId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByProperty(@PathVariable UUID propertyId) {
        return ResponseEntity.ok(reviewService.getReviewsByPropertyId(propertyId));
    }

    @GetMapping("/admin/search")
    public ResponseEntity<List<ReviewResponseDTO>> searchReviewsByGuestName(@RequestParam String guestName) {
        return ResponseEntity.ok(reviewService.searchReviewsByGuestName(guestName));
    }

    @DeleteMapping("/admin/{referenceId}")
    public ResponseEntity<Void> deleteReview(@PathVariable String referenceId) {
        reviewService.deleteReviewByReferenceId(referenceId);
        return ResponseEntity.ok().build();
    }

    // GUEST ENDPOINTS

    @PostMapping("/guest")
    public ResponseEntity<ReviewResponseDTO> createReview(@Valid @RequestBody ReviewRequestDTO reviewRequestDTO) {
        return ResponseEntity.ok(reviewService.createReview(reviewRequestDTO));
    }

    @GetMapping("/guest/{referenceId}")
    public ResponseEntity<ReviewResponseDTO> getMyReview(@PathVariable String referenceId) {
        return ResponseEntity.ok(reviewService.getMyReviewByReferenceId(referenceId));
    }

    @PutMapping("/guest/{referenceId}")
    public ResponseEntity<ReviewResponseDTO> updateMyReview(
            @PathVariable String referenceId,
            @Valid @RequestBody ReviewRequestDTO reviewRequestDTO) {
        return ResponseEntity.ok(reviewService.updateMyReviewByReferenceId(referenceId, reviewRequestDTO));
    }

    @DeleteMapping("/guest/{referenceId}")
    public ResponseEntity<Void> deleteMyReview(@PathVariable String referenceId) {
        reviewService.deleteMyReviewByReferenceId(referenceId);
        return ResponseEntity.ok().build();
    }

    // ====================== PROPERTY RATING ENDPOINTS ======================

    @GetMapping("/public/property/{propertyId}/rating")
    public ResponseEntity<ReviewService.PropertyRatingResponse> getPropertyRating(@PathVariable UUID propertyId) {
        return ResponseEntity.ok(reviewService.getPropertyRating(propertyId));
    }

    @GetMapping("/public/booking/{referenceId}/property-rating")
    public ResponseEntity<ReviewService.PropertyRatingResponse> getPropertyRatingByBookingReference(@PathVariable String referenceId) {
        return ResponseEntity.ok(reviewService.getPropertyRatingByBookingReferenceId(referenceId));
    }

    @GetMapping("/admin/property-ratings")
    public ResponseEntity<List<ReviewService.PropertyRatingResponse>> getAllPropertyRatings() {
        return ResponseEntity.ok(reviewService.getAllPropertyRatings());
    }

    // ====================== PUBLIC ENDPOINTS ======================

    @GetMapping("/public/statistics")
    public ResponseEntity<ReviewService.ReviewStatistics> getReviewStatistics() {
        return ResponseEntity.ok(reviewService.getReviewStatistics());
    }
}