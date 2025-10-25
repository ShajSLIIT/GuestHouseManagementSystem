-- V2: Create property table with manager_id foreign key
CREATE TABLE IF NOT EXISTS property (
                                        property_id BINARY(16) NOT NULL,
    name VARCHAR(255) NULL,
    description VARCHAR(255) NULL,
    location VARCHAR(255) NULL,
    city VARCHAR(255) NULL,
    country VARCHAR(255) NULL,
    is_active BIT(1) NULL,
    phone_number VARCHAR(255) NULL,
    total_rooms INT NULL,
    check_in_time TIME NULL,
    email VARCHAR(255) NOT NULL,
    check_out_time TIME NULL,
    cover_image_url VARCHAR(255) NULL,
    manager_id BINARY(16) NULL,
    CONSTRAINT pk_property PRIMARY KEY (property_id),
    CONSTRAINT fk_property_manager FOREIGN KEY (manager_id) REFERENCES admin_user (id)
    );
