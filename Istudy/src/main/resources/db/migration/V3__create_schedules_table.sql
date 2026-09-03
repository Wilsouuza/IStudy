CREATE TABLE schedules(

    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(150),
    start_date DATE NOT NULL,
    end_date DATE,
    CONSTRAINT fk_schedules_user FOREIGN KEY (user_id) REFERENCES users(id)
);