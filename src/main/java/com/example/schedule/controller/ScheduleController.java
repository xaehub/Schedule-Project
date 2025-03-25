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

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto dto) {

        return new ResponseEntity<>(scheduleService.saveSchedule(dto), HttpStatus.CREATED);

    }

    @GetMapping
    public List<ScheduleResponseDto> findAllSchedule() {
        return scheduleService.findAllSchedules();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> findMemoById(@PathVariable Long id) {

        return new ResponseEntity<>(scheduleService.findScheduleById(id), HttpStatus.OK);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<ScheduleResponseDto> updateSchedule(
//            @PathVariable Long id,
//            @RequestBody ScheduleRequestDto dto
//    ) {
//        return new ResponseEntity<>(scheduleService.updateSchedule(id,dto.getToDo(), dto.getWriter(), dto.getPassword(),))
//    }

}
