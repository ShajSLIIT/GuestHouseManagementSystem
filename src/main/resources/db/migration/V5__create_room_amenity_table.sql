-- V5: Create room_amenity table (depends on room and amenity)
CREATE TABLE IF NOT EXISTS room_amenity (
    id BINARY(16) NOT NULL,
    room_id BINARY(16) NOT NULL,
    amenity_id BINARY(16) NOT NULL,
    is_enabled BIT(1) NOT NULL DEFAULT 1,
    CONSTRAINT pk_room_amenity PRIMARY KEY (id),
    CONSTRAINT fk_room_amenity_room FOREIGN KEY (room_id) REFERENCES room (room_id),
    CONSTRAINT fk_room_amenity_amenity FOREIGN KEY (amenity_id) REFERENCES amenity (amenity_id),
    UNIQUE INDEX uk_room_amenity (room_id, amenity_id)
    );
