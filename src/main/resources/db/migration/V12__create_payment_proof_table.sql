-- V12: Create payment_proof table (depends on booking)
CREATE TABLE IF NOT EXISTS payment_proof (
    id BINARY(16) NOT NULL,
    booking_id BINARY(16) NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_url VARCHAR(500) NOT NULL,
    file_size BIGINT NULL,
    file_type VARCHAR(100) NULL,
    original_file_name VARCHAR(255) NULL,
    uploaded_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_payment_proof PRIMARY KEY (id),
    CONSTRAINT fk_payment_proof_booking FOREIGN KEY (booking_id) REFERENCES booking (booking_id) ON DELETE CASCADE,
    UNIQUE INDEX uk_payment_proof_booking (booking_id ASC) VISIBLE
    );