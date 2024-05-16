CREATE TABLE folder (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    parent_folder_id BIGINT
);

CREATE TABLE file (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    path VARCHAR(255) NOT NULL,
    extension VARCHAR(50) NOT NULL,
    folder_id BIGINT,
    user_id BIGINT
);