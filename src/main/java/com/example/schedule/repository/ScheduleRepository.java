package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;

import java.util.List;

public interface ScheduleRepository {
    ScheduleResponseDto saveSchedule(Schedule schedule);

    List<ScheduleResponseDto> findAllSchedules(String updatedAt, String writer);

    Schedule findScheduleByIDOrElseThrow(Long id);

    int updateSchedule(Long id, String to_do,String writer, String password);

    int deleteSchedule(Long id);

}
