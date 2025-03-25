package com.example.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Schedule {

    private Long id;
    private String toDo;
    private String writer;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Schedule(String toDo, String writer, String password, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.toDo = toDo;
        this.writer = writer;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }



}
