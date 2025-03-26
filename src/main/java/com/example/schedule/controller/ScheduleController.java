package com.example.schedule.controller;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 생성자 주입
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 일정 생성 API
    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto dto) {

        // 일정 생성하고 받은 데이터 반환
        return new ResponseEntity<>(scheduleService.saveSchedule(dto), HttpStatus.CREATED);

    }

    // 일정 목록 조회 API
    @GetMapping
    public List<ScheduleResponseDto> findAllSchedule(
            // 파라미터로 작성자 또는 수정일 request 받음
            @RequestParam(required = false) String updatedAt,   // 수정일 필수 X
            @RequestParam(required = false) String writer       // 작성자명 필수 X
    ) {

        // 전체 일정 목록 반환
        return scheduleService.findAllSchedules(updatedAt, writer);
    }

    // 선택 일정 조회 API
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findMemoById(@PathVariable Long id) {

        // 선택한 일정 반환
        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    }

    // 일정 수정 API
    @PatchMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto dto
    ) {

        // 일정 수정하고 받은 데이터 반환
        return new ResponseEntity<>(scheduleService.updateSchedule(id,dto.getToDo(), dto.getWriter(), dto.getPassword()), HttpStatus.OK);
    }

    // 일정 삭제 API
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleRequestDto dto
    ) {

        // 비밀번호 검사 후 맞으면 삭제
        scheduleService.deleteSchedule(id, dto.getPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
