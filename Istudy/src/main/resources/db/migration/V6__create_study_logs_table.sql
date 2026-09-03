CREATE TABLE study_logs(

    id BIGSERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    log_date DATE NOT NULL ,
    hours_studied DECIMAL(5,2) NOT NULL ,

    CONSTRAINT fk_study_logs_task FOREIGN KEY (task_id) REFERENCES tasks(id),
    CONSTRAINT fk_study_logs_subject FOREIGN KEY (subject_id) REFERENCES subjects(id)

);