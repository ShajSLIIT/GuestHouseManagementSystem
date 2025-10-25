-- V14: Create booking_status_history table (depends on booking)
CREATE TABLE IF NOT EXISTS booking_status_history (
    id BINARY(16) NOT NULL,
    booking_id BINARY(16) NOT NULL,
    from_status ENUM('pending', 'confirmed', 'cancelled', 'expired') NULL DEFAULT NULL,
    to_status ENUM('pending', 'confirmed', 'cancelled', 'expired') NOT NULL,
    changed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reason TEXT,
    PRIMARY KEY (id),
    INDEX idx_bsh_booking (booking_id ASC) VISIBLE,
    CONSTRAINT fk_bsh_booking FOREIGN KEY (booking_id) REFERENCES booking (booking_id) ON DELETE CASCADE ON UPDATE CASCADE
);
