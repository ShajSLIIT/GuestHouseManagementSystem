-- V3: Create room table with property_id foreign key
CREATE TABLE IF NOT EXISTS room (
                                    room_id BINARY(16) NOT NULL,
    property_id BINARY(16) NOT NULL,
    room_type VARCHAR(100) NULL,
    room_number VARCHAR(50) NULL,
    description TEXT NULL,
    price_per_night DECIMAL(10, 2) NULL,
    max_occupancy INT NULL,
    is_available BIT(1) NULL,
    image_url VARCHAR(255) NULL,
    CONSTRAINT pk_room PRIMARY KEY (room_id),
    CONSTRAINT FK_ROOM_PROPERTY FOREIGN KEY (property_id) REFERENCES property (property_id)
    );
