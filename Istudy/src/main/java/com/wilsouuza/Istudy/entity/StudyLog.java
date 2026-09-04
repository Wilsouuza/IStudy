package com.wilsouuza.Istudy.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "study_logs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id" , nullable = false)
    private Task taskId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subjectId;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "hours_studied", nullable = false, precision = 5, scale = 2)
    private BigDecimal hoursStudied;
}
