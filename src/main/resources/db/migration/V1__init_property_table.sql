CREATE TABLE property (
                          property_id     BINARY(16) NOT NULL,
                          name            VARCHAR(255),
                          description     TEXT,
                          location        VARCHAR(255),
                          city            VARCHAR(255),
                          country         VARCHAR(255),
                          is_active       BOOLEAN,
                          phone_number    VARCHAR(50),
                          total_rooms     INT,
                          email           VARCHAR(255) NOT NULL,
                          check_in_time   TIME,
                          check_out_time  TIME,
                          coverImageUrl   VARCHAR(512),
                          PRIMARY KEY (property_id)
);

CREATE TABLE room (
                      room_id         BINARY(16)  NOT NULL,
                      property_id     BINARY(16)  NOT NULL,
                      room_type       VARCHAR(100),
                      room_number     VARCHAR(50),
                      description     TEXT,
                      price_per_night DECIMAL(10,2),
                      max_occupancy   INT,
                      is_available    TINYINT(1),             -- maps to Boolean
                      image_url       VARCHAR(255),

                      PRIMARY KEY (room_id),

                      CONSTRAINT fk_room_property
                          FOREIGN KEY (property_id)
                              REFERENCES property (property_id)
                              ON UPDATE RESTRICT
                              ON DELETE RESTRICT
);

-- Helpful index & uniqueness so each property can’t reuse the same room number
CREATE INDEX idx_room_property ON room(property_id);
CREATE UNIQUE INDEX uq_room_property_number ON room(property_id, room_number);



CREATE TABLE amenity (
                         id          BINARY(16)   NOT NULL,
                         name        VARCHAR(100) NOT NULL,
                         description TEXT,
                         icon_url    VARCHAR(255),
                         is_active   TINYINT(1),

                         PRIMARY KEY (id),
                         UNIQUE KEY uq_amenity_name (name)
) ;

