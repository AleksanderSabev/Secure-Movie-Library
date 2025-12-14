CREATE DATABASE IF NOT EXISTS movielib;

USE movielib;

CREATE TABLE movies (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   title VARCHAR(255) NOT NULL,
   director VARCHAR(255),
   release_year INT CHECK (release_year BETWEEN 1888 AND 2100),
   rating DOUBLE PRECISION NULL
);

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL
);
