package com.wilsouuza.Istudy.repository;

import com.wilsouuza.Istudy.entity.StudyLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyLogRepository extends JpaRepository<StudyLog, Long> {
    List<StudyLog> findByTaskId(Long taskId);
    List<StudyLog> findBySubjectId(Long subjectId);
}
