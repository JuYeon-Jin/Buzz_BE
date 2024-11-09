show databases;
use buzz_database;
show tables;

CREATE TABLE users (
    user_id CHAR(36) NOT NULL PRIMARY KEY,
    username VARCHAR(30) NOT NULL,
    password VARCHAR(60) NOT NULL,
    name VARCHAR(30) NOT NULL,
    signup_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT 0
);
SELECT * FROM users;

CREATE TABLE community_category (
    category_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(30) NOT NULL
);
SELECT * FROM community_category;

CREATE TABLE community (
    post_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    user_id CHAR(36) NOT NULL,
    title VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    content TEXT NOT NULL,
    views INT DEFAULT 0,
    is_deleted BOOLEAN DEFAULT 0,
    FOREIGN KEY (category_id) REFERENCES community_category(category_id) ON DELETE RESTRICT ON UPDATE CASCADE
);
SELECT * FROM community;

CREATE TABLE files (
    file_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_size INT NOT NULL,
    content_type VARCHAR(255) NOT NULL,
    file_path VARCHAR(255) NOT NULL,
    FOREIGN KEY (post_id) REFERENCES community(post_id) ON DELETE CASCADE ON UPDATE CASCADE
);
SELECT * FROM files;

CREATE TABLE comments (
    comment_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    user_id CHAR(36) NOT NULL,
    content VARCHAR(500) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (post_id) REFERENCES community(post_id) ON DELETE RESTRICT ON UPDATE CASCADE
);
SELECT * FROM comments;


