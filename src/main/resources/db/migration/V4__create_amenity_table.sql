CREATE TABLE IF NOT EXISTS amenity (
                                       amenity_id BINARY(16) NOT NULL,
    name VARCHAR(100) NULL,
    description TEXT NULL,
    icon_url VARCHAR(255) NULL,
    is_active BIT(1) NULL,
    CONSTRAINT pk_amenity PRIMARY KEY (amenity_id)
    );
