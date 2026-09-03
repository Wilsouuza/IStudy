CREATE TABLE goals(

    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    description VARCHAR(500) NOT NULL ,
    target_hours INT NOT NULL ,
    week_reference DATE NOT NULL ,
    CONSTRAINT fk_goals_user FOREIGN KEY (user_id) REFERENCES users(id)

);