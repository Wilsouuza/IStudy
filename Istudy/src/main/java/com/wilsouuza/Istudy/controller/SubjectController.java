package com.wilsouuza.Istudy.controller;


import com.wilsouuza.Istudy.dto.request.SubjectRequest;
import com.wilsouuza.Istudy.dto.response.SubjectResponse;
import com.wilsouuza.Istudy.entity.Subject;
import com.wilsouuza.Istudy.entity.User;
import com.wilsouuza.Istudy.repository.SubjectRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController {

    private final SubjectRepository subjectRepository;

    public SubjectController(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @PostMapping
    public ResponseEntity<SubjectResponse> create(@Valid @RequestBody SubjectRequest request,
                                                  @AuthenticationPrincipal User currentUser) {

        Subject subject = new Subject();
        subject.setUser(currentUser);
        subject.setName(request.name());
        subject.setDescription(request.description());

        subjectRepository.save(subject);

        return ResponseEntity.ok(toResponse(subject));
    }

    public ResponseEntity<List<SubjectResponse>> findAll(@AuthenticationPrincipal User currentUser) {
        List<SubjectResponse> subjects = subjectRepository.findByUserId(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Subject not found"));

        if (!subject.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(toResponse(subject));
    }


    @PutMapping("/{id}")
    public ResponseEntity<SubjectResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SubjectRequest request,
            @AuthenticationPrincipal User currentUser
    ){
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Subject not found"));

        if (!subject.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }
        subject.setName(request.name());
        subject.setDescription(request.description());
        subjectRepository.save(subject);

        return ResponseEntity.ok(toResponse(subject));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser
    ){
        Subject subject =  subjectRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Subject not found"));

        if (!subject.getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(403).build();
        }

        subjectRepository.delete(subject);
        return ResponseEntity.ok().build();
    }

    private SubjectResponse toResponse(Subject subject) {
        return new SubjectResponse(subject.getId(), subject.getName(), subject.getDescription());
    }
}
