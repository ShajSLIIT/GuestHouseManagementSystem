CREATE TABLE room_amenity (
                              id         BINARY(16) NOT NULL,
                              room_id    BINARY(16) NOT NULL,
                              amenity_id BINARY(16) NOT NULL,
                              is_enabled TINYINT(1),

                              PRIMARY KEY (id),

                              CONSTRAINT fk_roomamenity_room
                                  FOREIGN KEY (room_id)
                                      REFERENCES room (room_id)
                                      ON UPDATE RESTRICT
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_roomamenity_amenity
                                  FOREIGN KEY (amenity_id)
                                      REFERENCES amenity (id)
                                      ON UPDATE RESTRICT
                                      ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Helpful indexes
CREATE INDEX idx_roomamenity_room ON room_amenity(room_id);
CREATE INDEX idx_roomamenity_amenity ON room_amenity(amenity_id);
