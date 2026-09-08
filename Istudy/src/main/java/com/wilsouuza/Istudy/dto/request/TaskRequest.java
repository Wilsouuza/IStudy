package com.wilsouuza.Istudy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskRequest(

        @NotNull(message = "Subject id is required")
        Long subjectId,

        @NotNull(message = "Schedule id is required")
        Long scheduleId,

        @NotBlank(message = "Title is required")
        String title,

        @NotNull(message = "Due date is required")
        LocalDate dueDate
) {
}
