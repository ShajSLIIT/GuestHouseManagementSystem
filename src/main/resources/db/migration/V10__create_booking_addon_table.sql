-- V10: Create booking_addon table (depends on booking and addon)
CREATE TABLE IF NOT EXISTS booking_addon (
    id BINARY(16) NOT NULL,
    booking_id BINARY(16) NOT NULL,
    addon_id BINARY(16) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_booking_addon PRIMARY KEY (id),
    CONSTRAINT fk_booking_addon_booking FOREIGN KEY (booking_id) REFERENCES booking (booking_id) ON DELETE CASCADE,
    CONSTRAINT fk_booking_addon_addon FOREIGN KEY (addon_id) REFERENCES addon (id) ON DELETE RESTRICT,
    UNIQUE INDEX uk_booking_addon (booking_id, addon_id)
    );