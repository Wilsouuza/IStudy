package com.wilsouuza.Istudy.controller;

import com.wilsouuza.Istudy.dto.request.StudyLogRequest;
import com.wilsouuza.Istudy.dto.response.StudyLogResponse;
import com.wilsouuza.Istudy.entity.StudyLog;
import com.wilsouuza.Istudy.entity.Subject;
import com.wilsouuza.Istudy.entity.Task;
import com.wilsouuza.Istudy.entity.User;
import com.wilsouuza.Istudy.repository.StudyLogRepository;
import com.wilsouuza.Istudy.repository.SubjectRepository;
import com.wilsouuza.Istudy.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/study-logs")
public class StudyLogController {

    private final StudyLogRepository studyLogRepository;
    private final TaskRepository taskRepository;
    private final SubjectRepository subjectRepository;

    public StudyLogController(StudyLogRepository studyLogRepository,
                              TaskRepository taskRepository,
                              SubjectRepository subjectRepository) {
        this.studyLogRepository = studyLogRepository;
        this.taskRepository = taskRepository;
        this.subjectRepository = subjectRepository;
    }

    @PostMapping
    public ResponseEntity<StudyLogResponse> create(
            @Valid @RequestBody StudyLogRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        Task task = taskRepository.findById(request.taskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));
        Subject subject = subjectRepository.findById(request.subjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        if (!task.getSubject().getUser().getId().equals(currentUser.getId())
                || !subject.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        StudyLog studyLog = new StudyLog();
        studyLog.setTask(task);
        studyLog.setSubject(subject);
        studyLog.setLogDate(request.logDate());
        studyLog.setHoursStudied(request.hoursStudied());

        studyLogRepository.save(studyLog);

        return ResponseEntity.ok(toResponse(studyLog));
    }

    @GetMapping
    public ResponseEntity<List<StudyLogResponse>> findBySubject(
            @RequestParam Long subjectId,
            @AuthenticationPrincipal User currentUser
    ) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        if (!subject.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        List<StudyLogResponse> logs = studyLogRepository.findBySubjectId(subjectId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(logs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyLogResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        StudyLog studyLog = studyLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("StudyLog not found"));

        if (!studyLog.getSubject().getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(toResponse(studyLog));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudyLogResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody StudyLogRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        StudyLog studyLog = studyLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("StudyLog not found"));

        if (!studyLog.getSubject().getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        Task task = taskRepository.findById(request.taskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));
        Subject subject = subjectRepository.findById(request.subjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        if (!task.getSubject().getUser().getId().equals(currentUser.getId())
                || !subject.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        studyLog.setTask(task);
        studyLog.setSubject(subject);
        studyLog.setLogDate(request.logDate());
        studyLog.setHoursStudied(request.hoursStudied());
        studyLogRepository.save(studyLog);

        return ResponseEntity.ok(toResponse(studyLog));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        StudyLog studyLog = studyLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("StudyLog not found"));

        if (!studyLog.getSubject().getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        studyLogRepository.delete(studyLog);

        return ResponseEntity.noContent().build();
    }

    private StudyLogResponse toResponse(StudyLog studyLog) {
        return new StudyLogResponse(
                studyLog.getId(),
                studyLog.getTask().getId(),
                studyLog.getSubject().getId(),
                studyLog.getLogDate(),
                studyLog.getHoursStudied()
        );
    }
}