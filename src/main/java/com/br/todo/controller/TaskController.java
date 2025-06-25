package com.br.todo.controller;

import com.br.todo.dto.task.TaskCreateDTO;
import com.br.todo.dto.task.TaskDTO;
import com.br.todo.dto.task.TaskUpdateDTO;
import com.br.todo.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @GetMapping("/by-status")
    public ResponseEntity<List<TaskDTO>> findAllTasksByUserAndStatus(
            @RequestHeader(name = "authorization") String authorization,
            @RequestParam(name = "status", defaultValue = "P") String status
    ) {
        return new ResponseEntity<>(taskService.findAllByUserAndStatus(authorization, status), HttpStatus.OK);
    }

    @GetMapping("/by-nome")
    public ResponseEntity<List<TaskDTO>> findAllTasksByUserAndNomeTarefa(
            @RequestHeader(name = "authorization") String authorization,
            @RequestParam(name = "nome", defaultValue = "") String nomeTarefa
    ) {
        return new ResponseEntity<>(taskService.findAllByUserAndNomeTarefa(authorization, nomeTarefa), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(
            @Valid @RequestBody TaskCreateDTO taskCreateDTO,
            @RequestHeader(name = "authorization") String authorization) throws IOException {
        return new ResponseEntity<>(taskService.create(taskCreateDTO, authorization), HttpStatus.CREATED);
    }

    @PutMapping("/{idTask}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable Integer idTask,
            @RequestBody @Valid TaskUpdateDTO taskUpdateDTO,
            @RequestHeader(name = "authorization") String authorization) {
        return new ResponseEntity<>(taskService.updateTask(idTask, taskUpdateDTO, authorization), HttpStatus.OK);
    }

    @PatchMapping("/{idTask}/complete")
    public ResponseEntity<Void> completeTask(@PathVariable Integer idTask, @RequestHeader(name = "authorization") String authorization) {
        taskService.markTaskAsComplete(idTask, authorization);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{idTask}/uncomplete")
    public ResponseEntity<Void> uncompleteTask(@PathVariable Integer idTask, @RequestHeader(name = "authorization") String authorization) {
        taskService.markTaskAsUncomplete(idTask, authorization);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{idTask}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Integer idTask,
            @RequestHeader(name = "authorization") String authorization
    ) {
        taskService.deleteTask(idTask, authorization);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
