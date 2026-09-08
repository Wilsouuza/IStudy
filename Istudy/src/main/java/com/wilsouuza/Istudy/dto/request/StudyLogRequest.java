package com.wilsouuza.Istudy.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record StudyLogRequest(
        @NotNull(message = "Task id is required")
        Long taskId,

        @NotNull(message = "Subject id is required")
        Long subjectId,

        @NotNull(message = "Log date is required")
        LocalDate logDate,

        @NotNull(message = "Hours studied is required")
        @DecimalMin(value = "0.01", message = "Hours studied must be greater than zero")
        BigDecimal hoursStudied
) {
}
