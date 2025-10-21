-- Create payment_proof table for storing payment slips
CREATE TABLE IF NOT EXISTS payment_proof (
                                             id BINARY(16) PRIMARY KEY,
    bookingId BINARY(16) NOT NULL,
    fileUrl VARCHAR(255) NOT NULL,
    uploadedAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
    );

-- Create indexes for better performance
CREATE INDEX idx_payment_proof_booking_id ON payment_proof(bookingId);
CREATE INDEX idx_payment_proof_uploaded_at ON payment_proof(uploadedAt);