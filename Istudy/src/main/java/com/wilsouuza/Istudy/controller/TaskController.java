package com.wilsouuza.Istudy.controller;

import com.wilsouuza.Istudy.dto.request.TaskRequest;
import com.wilsouuza.Istudy.dto.response.TaskResponse;
import com.wilsouuza.Istudy.entity.Schedule;
import com.wilsouuza.Istudy.entity.Subject;
import com.wilsouuza.Istudy.entity.Task;
import com.wilsouuza.Istudy.entity.User;
import com.wilsouuza.Istudy.repository.ScheduleRepository;
import com.wilsouuza.Istudy.repository.SubjectRepository;
import com.wilsouuza.Istudy.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final SubjectRepository subjectRepository;
    private final ScheduleRepository scheduleRepository;

    public TaskController(TaskRepository taskRepository,
                          SubjectRepository subjectRepository,
                          ScheduleRepository scheduleRepository) {
        this.taskRepository = taskRepository;
        this.subjectRepository = subjectRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        Subject subject = subjectRepository.findById(request.subjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        Schedule schedule = scheduleRepository.findById(request.scheduleId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!subject.getUser().getId().equals(currentUser.getId())
                || !schedule.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        Task task = new Task();
        task.setSubject(subject);
        task.setSchedule(schedule);
        task.setTitle(request.title());
        task.setDueDate(request.dueDate());
        task.setCompleted(false);

        taskRepository.save(task);

        return ResponseEntity.ok(toResponse(task));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> findBySubject(
            @RequestParam Long subjectId,
            @AuthenticationPrincipal User currentUser
    ) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        if (!subject.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        List<TaskResponse> tasks = taskRepository.findBySubjectId(subjectId)
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getSubject().getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(toResponse(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getSubject().getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        Subject subject = subjectRepository.findById(request.subjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        Schedule schedule = scheduleRepository.findById(request.scheduleId())
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!subject.getUser().getId().equals(currentUser.getId())
                || !schedule.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        task.setSubject(subject);
        task.setSchedule(schedule);
        task.setTitle(request.title());
        task.setDueDate(request.dueDate());
        taskRepository.save(task);

        return ResponseEntity.ok(toResponse(task));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> markAsCompleted(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getSubject().getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        task.setCompleted(true);
        taskRepository.save(task);

        return ResponseEntity.ok(toResponse(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getSubject().getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        taskRepository.delete(task);

        return ResponseEntity.noContent().build();
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getSubject().getId(),
                task.getSchedule().getId(),
                task.getTitle(),
                task.getCompleted(),
                task.getDueDate()
        );
    }
}
