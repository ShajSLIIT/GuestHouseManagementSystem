-- V8: Create booking_room table (depends on booking and room)
CREATE TABLE IF NOT EXISTS booking_room (
    id BINARY(16) NOT NULL,
    booking_id BINARY(16) NOT NULL,
    room_id BINARY(16) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX uk_booking_room (booking_id ASC, room_id ASC) VISIBLE,
    INDEX idx_br_booking (booking_id ASC) VISIBLE,
    INDEX idx_br_room (room_id ASC) VISIBLE,
    CONSTRAINT fk_br_booking FOREIGN KEY (booking_id) REFERENCES booking (booking_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_br_room FOREIGN KEY (room_id) REFERENCES room (room_id) ON DELETE RESTRICT ON UPDATE CASCADE
);
