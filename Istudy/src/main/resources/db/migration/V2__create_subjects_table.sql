CREATE TABLE subjects (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    CONSTRAINT fk_subjects_user FOREIGN KEY (user_id) REFERENCES users(id)
);