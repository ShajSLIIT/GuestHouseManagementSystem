CREATE TABLE IF NOT EXISTS property
(
    property_id     BINARY(16)   NOT NULL,
    name            VARCHAR(255) NULL,
    `description`   VARCHAR(255) NULL,
    location        VARCHAR(255) NULL,
    city            VARCHAR(255) NULL,
    country         VARCHAR(255) NULL,
    is_active       BIT(1)       NULL,
    phone_number    VARCHAR(255) NULL,
    total_rooms     INT          NULL,
    check_in_time   time         NULL,
    email           VARCHAR(255) NOT NULL,
    check_out_time  time         NULL,
    cover_image_url VARCHAR(255) NULL,
    CONSTRAINT pk_property PRIMARY KEY (property_id)
);

CREATE TABLE IF NOT EXISTS booking (
    booking_id          BINARY(16) NOT NULL,
    property_id         BINARY(16) NOT NULL,
    reference_id        VARCHAR(100) NOT NULL,
    guest_name          VARCHAR(200) NOT NULL,
    guest_email         VARCHAR(100) NULL DEFAULT NULL,
    guest_phone         VARCHAR(20) NULL DEFAULT NULL,
    check_in_date       DATE NOT NULL,
    check_out_date      DATE NOT NULL,
    created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    status              ENUM('pending', 'confirmed', 'cancelled', 'expired') NOT NULL DEFAULT 'pending',
    no_of_rooms         INT(5) NOT NULL DEFAULT '1',
    no_of_guests        INT(5) NOT NULL DEFAULT '1',
    total_price         DECIMAL(10,2) NOT NULL,
    customer_unique_id  VARCHAR(100) NULL DEFAULT NULL,
    is_paid             BIT(1) NOT NULL DEFAULT 0,
    confirmed_at        DATETIME NULL DEFAULT NULL,
    expired_at          DATETIME NULL DEFAULT NULL,
    notes               TEXT NULL DEFAULT NULL,
    token               VARCHAR(100) NULL DEFAULT NULL,
    CONSTRAINT pk_booking PRIMARY KEY (booking_id),
    UNIQUE INDEX reference_id (reference_id ASC) VISIBLE,
    INDEX idx_booking_property (property_id ASC) VISIBLE,
    INDEX idx_booking_dates (check_in_date ASC, check_out_date ASC) VISIBLE,
    CONSTRAINT fk_booking_property FOREIGN KEY (property_id) REFERENCES property (property_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS room
(
    room_id         BINARY(16)     NOT NULL,
    property_id     BINARY(16)     NOT NULL,
    room_type       VARCHAR(100)   NULL,
    room_number     VARCHAR(50)    NULL,
    `description`   TEXT           NULL,
    price_per_night DECIMAL(10, 2) NULL,
    max_occupancy   INT            NULL,
    is_available    BIT(1)         NOT NULL DEFAULT 1,
    image_url       VARCHAR(255)   NULL,
    CONSTRAINT pk_room PRIMARY KEY (room_id),
    CONSTRAINT FK_ROOM_PROPERTY FOREIGN KEY (property_id) REFERENCES property (property_id)
);

CREATE TABLE IF NOT EXISTS booking_room(
    id          BINARY(16) NOT NULL,
    booking_id  BINARY(16) NOT NULL,
    room_id     BINARY(16) NOT NULL,
    start_date  DATE NOT NULL,
    end_date    DATE NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX uk_booking_room (booking_id ASC, room_id ASC) VISIBLE,
    INDEX idx_br_booking (booking_id ASC) VISIBLE,
    INDEX idx_br_room (room_id ASC) VISIBLE,
    CONSTRAINT fk_br_booking FOREIGN KEY (booking_id) REFERENCES booking (booking_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_br_room FOREIGN KEY (room_id) REFERENCES room (room_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS temporary_token (
    id          BINARY(16) NOT NULL,
    booking_id  BINARY(16) NOT NULL,
    token       VARCHAR(100) NOT NULL,
    expires_at  DATE NOT NULL,
    is_active   BIT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE INDEX uk_temp_token (token ASC) VISIBLE,
    INDEX idx_temp_token_booking (booking_id ASC) VISIBLE,
    CONSTRAINT fk_temp_token_booking FOREIGN KEY (booking_id) REFERENCES booking (booking_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS booking_status_history (
    id          BINARY(16) NOT NULL,
    booking_id  BINARY(16) NOT NULL,
    from_status ENUM('pending', 'confirmed', 'cancelled', 'expired') NULL DEFAULT NULL,
    to_status   ENUM('pending', 'confirmed', 'cancelled', 'expired') NOT NULL,
    changed_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reason      TEXT,
    PRIMARY KEY (id),
    INDEX idx_bsh_booking (booking_id ASC) VISIBLE,
    CONSTRAINT fk_bsh_booking FOREIGN KEY (booking_id) REFERENCES booking (booking_id) ON DELETE CASCADE ON UPDATE CASCADE
);