-- V9: Create addon table (no dependencies)
CREATE TABLE IF NOT EXISTS addon (
    id BINARY(16) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT NULL,
    price DECIMAL(10,2) NOT NULL,
    active BIT(1) NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT pk_addon PRIMARY KEY (id),
    UNIQUE INDEX uk_addon_name (name ASC) VISIBLE
    );