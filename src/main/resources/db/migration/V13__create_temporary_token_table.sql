-- V13: Create temporary_token table (depends on booking)
CREATE TABLE IF NOT EXISTS temporary_token (
    id BINARY(16) NOT NULL,
    booking_id BINARY(16) NOT NULL,
    token VARCHAR(100) NOT NULL,
    expires_at DATE NOT NULL,
    is_active BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE INDEX uk_temp_token (token ASC) VISIBLE,
    INDEX idx_temp_token_booking (booking_id ASC) VISIBLE,
    CONSTRAINT fk_temp_token_booking FOREIGN KEY (booking_id) REFERENCES booking (booking_id) ON DELETE CASCADE ON UPDATE CASCADE
);
