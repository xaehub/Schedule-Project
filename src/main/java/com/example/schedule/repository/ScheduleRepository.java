package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;

public interface ScheduleRepository {
    ScheduleResponseDto saveSchedule(Schedule schedule);
}
