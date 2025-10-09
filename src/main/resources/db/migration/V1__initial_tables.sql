-- Create addon table with proper UUID storage
CREATE TABLE addon (
                       id BINARY(16) PRIMARY KEY,
                       name VARCHAR(255) NOT NULL UNIQUE,
                       description TEXT,
                       price DECIMAL(10,2) NOT NULL,
                       active BOOLEAN DEFAULT TRUE,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create booking table
CREATE TABLE booking (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         guest_name VARCHAR(255) NOT NULL,
                         guest_email VARCHAR(255) NOT NULL,
                         guest_phone VARCHAR(20),
                         check_in_date DATE NOT NULL,
                         check_out_date DATE NOT NULL,
                         total_price DECIMAL(10,2) NOT NULL,
                         status ENUM('PENDING', 'CONFIRMED', 'CANCELLED', 'COMPLETED') DEFAULT 'PENDING',
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create booking_addon table with compatible UUID type
CREATE TABLE booking_addon (
                               id BINARY(16) PRIMARY KEY,
                               booking_id BIGINT NOT NULL,
                               addon_id BINARY(16) NOT NULL,
                               quantity INT NOT NULL DEFAULT 1,
                               unit_price DECIMAL(10,2) NOT NULL,
                               total_price DECIMAL(10,2) NOT NULL,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               FOREIGN KEY (booking_id) REFERENCES booking(id) ON DELETE CASCADE,
                               FOREIGN KEY (addon_id) REFERENCES addon(id) ON DELETE CASCADE,
                               UNIQUE KEY unique_booking_addon (booking_id, addon_id)
);

-- Create review table
CREATE TABLE review (
                        id BINARY(16) PRIMARY KEY,
                        booking_id BIGINT NOT NULL,
                        rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
                        comment TEXT,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (booking_id) REFERENCES booking(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_addon_active ON addon(active);
CREATE INDEX idx_addon_name ON addon(name);
CREATE INDEX idx_booking_dates ON booking(check_in_date, check_out_date);
CREATE INDEX idx_booking_status ON booking(status);
CREATE INDEX idx_booking_addon_booking ON booking_addon(booking_id);
CREATE INDEX idx_booking_addon_addon ON booking_addon(addon_id);
CREATE INDEX idx_review_booking ON review(booking_id);

-- Insert sample addons with PROPER UUID format
INSERT INTO addon (id, name, description, price, active) VALUES
                                                             (UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), 'Breakfast Buffet', 'Daily continental breakfast with fresh fruits, pastries, and beverages', 12.99, TRUE),
                                                             (UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), 'Airport Transfer', 'Round-trip airport pickup and drop service', 35.00, TRUE),
                                                             (UUID_TO_BIN('33333333-3333-3333-3333-333333333333'), 'Spa Package', 'Full body massage and spa treatment for relaxation', 75.50, TRUE),
                                                             (UUID_TO_BIN('44444444-4444-4444-4444-444444444444'), 'Late Checkout', 'Extended checkout time until 2:00 PM', 25.00, TRUE),
                                                             (UUID_TO_BIN('55555555-5555-5555-5555-555555555555'), 'Room Service', '24/7 room service with premium menu options', 18.75, TRUE),
                                                             (UUID_TO_BIN('66666666-6666-6666-6666-666666666666'), 'Parking', 'Secure underground parking space', 15.00, TRUE),
                                                             (UUID_TO_BIN('77777777-7777-7777-7777-777777777777'), 'WiFi Premium', 'High-speed internet access throughout stay', 8.99, TRUE),
                                                             (UUID_TO_BIN('88888888-8888-8888-8888-888888888888'), 'Laundry Service', 'Daily laundry and dry cleaning service', 22.00, FALSE);

-- Insert sample bookings
INSERT INTO booking (guest_name, guest_email, guest_phone, check_in_date, check_out_date, total_price, status) VALUES
                                                                                                                   ('John Smith', 'john.smith@email.com', '+1-555-0101', '2025-10-15', '2025-10-18', 450.00, 'CONFIRMED'),
                                                                                                                   ('Maria Garcia', 'maria.garcia@email.com', '+1-555-0102', '2025-10-20', '2025-10-22', 300.00, 'PENDING'),
                                                                                                                   ('David Johnson', 'david.johnson@email.com', '+1-555-0103', '2025-10-12', '2025-10-14', 280.00, 'COMPLETED'),
                                                                                                                   ('Sarah Chen', 'sarah.chen@email.com', '+1-555-0104', '2025-10-25', '2025-10-30', 750.00, 'CONFIRMED'),
                                                                                                                   ('Robert Brown', 'robert.brown@email.com', '+1-555-0105', '2025-10-08', '2025-10-10', 320.00, 'CANCELLED'),
                                                                                                                   ('Emily Wilson', 'emily.wilson@email.com', '+1-555-0106', '2025-11-01', '2025-11-05', 600.00, 'PENDING');

-- Insert sample booking addons with PROPER UUID format
INSERT INTO booking_addon (id, booking_id, addon_id, quantity, unit_price, total_price) VALUES
                                                                                            (UUID_TO_BIN('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa'), 1, UUID_TO_BIN('11111111-1111-1111-1111-111111111111'), 3, 12.99, 38.97),
                                                                                            (UUID_TO_BIN('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'), 1, UUID_TO_BIN('22222222-2222-2222-2222-222222222222'), 1, 35.00, 35.00),
                                                                                            (UUID_TO_BIN('cccccccc-cccc-cccc-cccc-cccccccccccc'), 2, UUID_TO_BIN('77777777-7777-7777-7777-777777777777'), 2, 8.99, 17.98),
                                                                                            (UUID_TO_BIN('dddddddd-dddd-dddd-dddd-dddddddddddd'), 3, UUID_TO_BIN('33333333-3333-3333-3333-333333333333'), 1, 75.50, 75.50),
                                                                                            (UUID_TO_BIN('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee'), 3, UUID_TO_BIN('55555555-5555-5555-5555-555555555555'), 2, 18.75, 37.50),
                                                                                            (UUID_TO_BIN('ffffffff-ffff-ffff-ffff-ffffffffffff'), 4, UUID_TO_BIN('66666666-6666-6666-6666-666666666666'), 5, 15.00, 75.00),
                                                                                            (UUID_TO_BIN('99999999-9999-9999-9999-999999999999'), 4, UUID_TO_BIN('44444444-4444-4444-4444-444444444444'), 1, 25.00, 25.00);

-- Insert sample reviews with PROPER UUID format
INSERT INTO review (id, booking_id, rating, comment) VALUES
                                                         (UUID_TO_BIN('11111111-1111-1111-1111-111111111112'), 3, 5, 'Excellent service and comfortable stay. The spa package was amazing!'),
                                                         (UUID_TO_BIN('22222222-2222-2222-2222-222222222223'), 3, 4, 'Great experience overall. Room service was prompt and food quality was good.'),
                                                         (UUID_TO_BIN('33333333-3333-3333-3333-333333333334'), 1, 5, 'Outstanding hospitality. The breakfast buffet had great variety and quality.');