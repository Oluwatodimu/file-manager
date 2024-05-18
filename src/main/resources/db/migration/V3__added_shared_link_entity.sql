CREATE TABLE shared_link (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token BINARY(16) NOT NULL UNIQUE,
    is_public BIT NOT NULL,
    file_id BIGINT,
    folder_id BIGINT,
    user_id BIGINT
);