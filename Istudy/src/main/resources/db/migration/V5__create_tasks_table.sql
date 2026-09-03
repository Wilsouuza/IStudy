CREATE TABLE tasks(
    id BIGSERIAL PRIMARY KEY,
    subject_id BIGINT NOT NULL,
    schedule_id BIGINT,
    title VARCHAR(150) NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    due_date DATE NOT NULL,

    CONSTRAINT fk_tasks_subject FOREIGN KEY (subject_id) REFERENCES subjects(id),
    CONSTRAINT fk_tasks_schedule FOREIGN KEY (schedule_id) REFERENCES schedules(id)
);