package com.wilsouuza.Istudy.controller;

import com.wilsouuza.Istudy.dto.request.ScheduleRequest;
import com.wilsouuza.Istudy.dto.response.ScheduleResponse;
import com.wilsouuza.Istudy.entity.Schedule;
import com.wilsouuza.Istudy.entity.User;
import com.wilsouuza.Istudy.repository.ScheduleRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;

    public ScheduleController(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @PostMapping
    public ResponseEntity<ScheduleResponse> create(
            @Valid @RequestBody ScheduleRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        Schedule schedule = new Schedule();
        schedule.setUser(currentUser);
        schedule.setName(request.name());
        schedule.setStartDate(request.startDate());
        schedule.setEndDate(request.endDate());

        scheduleRepository.save(schedule);

        return ResponseEntity.ok(toResponse(schedule));
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> findAll(@AuthenticationPrincipal User currentUser) {
        List<ScheduleResponse> schedules = scheduleRepository.findByUserId(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(toResponse(schedule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        schedule.setName(request.name());
        schedule.setStartDate(request.startDate());
        schedule.setEndDate(request.endDate());
        scheduleRepository.save(schedule);

        return ResponseEntity.ok(toResponse(schedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        scheduleRepository.delete(schedule);

        return ResponseEntity.noContent().build();
    }

    private ScheduleResponse toResponse(Schedule schedule) {
        return new ScheduleResponse(
                schedule.getId(),
                schedule.getName(),
                schedule.getStartDate(),
                schedule.getEndDate()
        );
    }
}