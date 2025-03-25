package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
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

    // 생성자 주입
    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // 일정 저장 Service
    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {

        // 현재 시간 생성
        // 한번만 만들어서 createdAt과 updatedAt의 시간 차이를 없앤다
        LocalDateTime now = LocalDateTime.now();

        // Schedule 객체를 만들어서 DB에 저장
        Schedule schedule = new Schedule(dto.getToDo(), dto.getWriter(), dto.getPassword(), now, now);

        // 일정 저장 후 반환된 ScheduleResponseDto 반환
        return scheduleRepository.saveSchedule(schedule);
    }

    // 모든 일정 조회 Service
    @Override
    public List<ScheduleResponseDto> findAllSchedules()  {

        // 모든 일정 목록을 ScheduleResponseDto 형태로 반환
        return scheduleRepository.findAllSchedules();
    }

    // 선택 일정 조회 Service
    @Override
    public ScheduleResponseDto findScheduleById(Long id) {

        // 해당 id로 일정을 찾고, 없으면 예외를 던짐
        Schedule schedule = scheduleRepository.findScheduleByIDOrElseThrow(id);

        // 선택한 일정을 ScheduleResponseDto로 변환하여 반환
        return new ScheduleResponseDto(schedule);
    }

    // 일정 수정 Service
    @Transactional  // 트랜잭션을 지원하여 데이터 변경 시 모든 작업이 성공하거나 롤백되도록 함
    @Override
    public ScheduleResponseDto updateSchedule(Long id, String to_do, String writer, String password) {

        // 일정 조회 (일정이 없으면 예외 발생)
        Schedule schedule = scheduleRepository.findScheduleByIDOrElseThrow(id);

        // 비밀번호 유효성 검사
        if (!schedule.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 틀렸습니다.");
        }

        // 작성자 값이 없으면 오류 발생
        if(writer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "작성자는 필수값입니다.");
        }

        // 일정 수정
        int updatedRow = scheduleRepository.updateSchedule(id, to_do, writer, password);

        // 수정된 행이 없다면, id에 해당하는 일정이 없다는 예외 발생
        if(updatedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }

        // 수정된 일정을 다시 updatedSchedule 객체에 저장
        Schedule updatedSchedule = scheduleRepository.findScheduleByIDOrElseThrow(id);

        // 수정된 일정 조회 후 반환
        return new ScheduleResponseDto(updatedSchedule);
    }

    // 일정 삭제 Service
    @Override
    public void deleteSchedule(Long id, String password) {

        // 일정 조회
        Schedule schedule = scheduleRepository.findScheduleByIDOrElseThrow(id);

        // 비밀번호 유효성 검사
        if (!schedule.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀번호가 틀렸습니다.");
        }

        // 일정 삭제 후 삭제한 값 id 데이터 반환
        int deletedRow = scheduleRepository.deleteSchedule(id);

        // 반환받은 id값이 0이라면 예외처리(NOt_FOUND)
        if(deletedRow == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist id = " + id);
        }
    }
}
