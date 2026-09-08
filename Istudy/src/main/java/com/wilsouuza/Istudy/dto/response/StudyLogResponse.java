package com.wilsouuza.Istudy.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record StudyLogResponse(
        Long id,
        Long taskId,
        Long subjectId,
        LocalDate logDate,
        BigDecimal hoursStudied
) {
}
