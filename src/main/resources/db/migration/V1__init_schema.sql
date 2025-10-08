CREATE TABLE IF NOT EXISTS booking (
    booking_id BINARY(16) NOT NULL,
--     property_id BINARY(16) NOT NULL,
    reference_id VARCHAR(100) NOT NULL,
    guest_name VARCHAR(200) NOT NULL,
    guest_email VARCHAR(100) NULL DEFAULT NULL,
    guest_phone VARCHAR(20) NULL DEFAULT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status ENUM('pending', 'confirmed', 'cancelled', 'expired') NOT NULL DEFAULT 'pending',
    no_of_rooms INT(5) NOT NULL DEFAULT '1',
    no_of_guests INT(5) NOT NULL DEFAULT '1',
    total_price DECIMAL(10,2) NOT NULL,
    customer_unique_id VARCHAR(100) NULL DEFAULT NULL,
    is_paid TINYINT(1) NOT NULL DEFAULT '0',
    confirmed_at DATETIME NULL DEFAULT NULL,
    expired_at DATETIME NULL DEFAULT NULL,
    notes TEXT NULL DEFAULT NULL,
    token VARCHAR(100) NULL DEFAULT NULL,
    PRIMARY KEY (booking_id),
    UNIQUE INDEX reference_id (reference_id ASC) VISIBLE,
--     INDEX idx_booking_property (property_id ASC) VISIBLE,
    INDEX idx_booking_dates (check_in_date ASC, check_out_date ASC) VISIBLE
--     CONSTRAINT fk_booking_property FOREIGN KEY (property_id) REFERENCES property (property_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS booking_room(
    id BINARY(16) NOT NULL,
    booking_id BINARY(16) NOT NULL,
--     room_id BINARY(16) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    PRIMARY KEY (id),
--     UNIQUE INDEX uk_booking_room (booking_id ASC, room_id ASC) VISIBLE,
    INDEX idx_br_booking (booking_id ASC) VISIBLE,
--     INDEX idx_br_room (room_id ASC) VISIBLE,
    CONSTRAINT fk_br_booking FOREIGN KEY (booking_id) REFERENCES booking (booking_id) ON DELETE CASCADE ON UPDATE CASCADE
--     CONSTRAINT fk_br_room FOREIGN KEY (room_id) REFERENCES room (room_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS temporary_token (
    id BINARY(16) NOT NULL,
    booking_id BINARY(16) NOT NULL,
    token VARCHAR(100) NOT NULL,
    expires_at DATETIME NOT NULL,
    is_active TINYINT NOT NULL DEFAULT '0',
    PRIMARY KEY (id),
    UNIQUE INDEX uk_temp_token (token ASC) VISIBLE
--    INDEX idx_temp_token_booking (booking_id ASC) VISIBLE,
--    CONSTRAINT fk_temp_token_booking FOREIGN KEY (booking_id) REFERENCES booking (booking_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS booking_status_history (
    id BINARY(16) NOT NULL,
    booking_id BINARY(16) NOT NULL,
    from_status ENUM('pending', 'confirmed', 'cancelled', 'expired') NULL DEFAULT NULL,
    to_status ENUM('pending', 'confirmed', 'cancelled', 'expired') NOT NULL,
    changed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reason TEXT,
    PRIMARY KEY (id)
--    INDEX idx_bsh_booking (booking_id ASC) VISIBLE,
--    CONSTRAINT fk_bsh_booking FOREIGN KEY (booking_id) REFERENCES booking (booking_id) ON DELETE CASCADE ON UPDATE CASCADE
);