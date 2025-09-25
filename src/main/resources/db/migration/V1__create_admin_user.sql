-- -----------------------------------------------------
-- Table `guesthousemanagement`.`admin_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `guesthousemanagement`.`admin_user` (
    `id` BINARY(16) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NOT NULL,
    `passwordHash` VARCHAR(255) NOT NULL,
    `lastLogin` DATETIME NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `email` (`email` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;