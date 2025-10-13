

CREATE TABLE IF NOT EXISTS photo
(
    photo_id    BINARY(16)   NOT NULL,
    property_id BINARY(16)   NULL,
    room_id     BINARY(16)   NULL,
    image_url   VARCHAR(255) NOT NULL,
    caption     VARCHAR(255) NULL,
    is_main     BIT(1)       NULL,
    CONSTRAINT pk_photo PRIMARY KEY (photo_id)
    );

ALTER TABLE photo
    ADD CONSTRAINT FK_PHOTO_ON_PROPERTY FOREIGN KEY (property_id) REFERENCES property (property_id);

ALTER TABLE photo
    ADD CONSTRAINT FK_PHOTO_ON_ROOM FOREIGN KEY (room_id) REFERENCES room (room_id);