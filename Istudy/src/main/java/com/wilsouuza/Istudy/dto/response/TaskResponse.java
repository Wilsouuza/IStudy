package com.wilsouuza.Istudy.dto.response;

import java.time.LocalDate;

public record TaskResponse(
        Long id,
        Long subjectId,
        Long scheduleId,
        String title,
        boolean completed,
        LocalDate dueDate
) {
}
