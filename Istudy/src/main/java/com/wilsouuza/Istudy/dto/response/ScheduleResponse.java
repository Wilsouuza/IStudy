package com.wilsouuza.Istudy.dto.response;

import java.time.LocalDate;

public record ScheduleResponse(
        Long id,
        String name,
        LocalDate startDate,
        LocalDate endDate
) {
}
