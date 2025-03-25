package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.JdbcTemPlateScheduleRepository;
import com.example.schedule.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {

        LocalDateTime now = LocalDateTime.now();    // 한번만 만들어서 createdAt과 updatedAt의 시간 차이를 없앤다

        Schedule schedule = new Schedule(dto.getToDo(), dto.getWriter(), dto.getPassword(), now, now);

        return scheduleRepository.saveSchedule(schedule);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules()  {
        return scheduleRepository.findAllSchedules();
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {

        Schedule schedule = scheduleRepository.findScheduleByIDOrElseThrow(id);
        return new ScheduleResponseDto(schedule);
    }

    @Transactional
    @Override
    public ScheduleResponseDto updateSchedule(Long id, String to_do, String writer, String password) {

        Schedule schedule = scheduleRepository.findScheduleByIDOrElseThrow(id);

        // 비밀번호 유효성 검사
        if (!schedule.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 틀렸습니다.");
        }

        if(writer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자는 필수값입니다.");
        }


        int updatedRow = scheduleRepository.updateSchedule(id, to_do, writer, password);

        if(updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        Schedule schedule1 = scheduleRepository.findScheduleByIDOrElseThrow(id);

        return new ScheduleResponseDto(schedule1);
    }

    @Override
    public void deleteSchedule(Long id, String password) {
        int deletedRow = scheduleRepository.deleteSchedule(id);

        if(deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }
    }
}
