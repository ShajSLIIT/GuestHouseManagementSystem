
CREATE TABLE IF NOT EXISTS admin_user (

    id BINARY(16) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    last_login DATETIME NULL DEFAULT NULL,
    PRIMARY KEY(id),
    UNIQUE INDEX uk_admin_email (email ASC)
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;
