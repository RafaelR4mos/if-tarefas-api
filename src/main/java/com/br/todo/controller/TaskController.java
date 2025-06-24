package com.br.todo.controller;

import com.br.todo.dto.task.TaskDTO;
import com.br.todo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/by-user")
    public ResponseEntity<List<TaskDTO>> findAllTaskByUser(@RequestHeader(name = "authorization") String authorization) {
        return new ResponseEntity<>(taskService.findAllByUser(authorization), HttpStatus.OK);
    }
}
