CREATE DATABASE IF NOT EXISTS reliance_games;
USE reliance_games;

CREATE TABLE IF NOT EXISTS players (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_id VARCHAR(255) NOT NULL UNIQUE,
    user_name VARCHAR(255) NOT NULL,
    platform VARCHAR(255) NOT NULL,
    creation_date DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS player_progression (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_id BIGINT NOT NULL,
    level INT,
    `rank` INT,
    gold BIGINT,
    cash BIGINT,
    gem BIGINT,
    rewards_collected TEXT,
    last_active_time DATETIME,
    country VARCHAR(255),
    FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS scores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_id BIGINT NOT NULL,
    game_id BIGINT NOT NULL,
    score BIGINT NOT NULL,
    timestamp DATETIME NOT NULL,
    FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS game_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    configuration TEXT
);


