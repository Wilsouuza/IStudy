package com.wilsouuza.Istudy.repository;

import com.wilsouuza.Istudy.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findBySubjectId(Long id);
    List<Task> findByScheduleId(Long scheduleId);
}
