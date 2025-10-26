-- V11: Create review table (depends on booking)
CREATE TABLE IF NOT EXISTS review (
    id BINARY(16) NOT NULL,
    booking_id BINARY(16) NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_review PRIMARY KEY (id),
    CONSTRAINT fk_review_booking FOREIGN KEY (booking_id) REFERENCES booking (booking_id) ON DELETE CASCADE,
    UNIQUE INDEX uk_review_booking (booking_id ASC) VISIBLE
    );