package com.br.todo.service;

import com.br.todo.dto.task.TaskDTO;
import com.br.todo.entity.User;
import com.br.todo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ModelMapper mapper;

    public List<TaskDTO> findAllByUser(String authHeader) {
        User user = userService.getLoggedUser(authHeader);

        return taskRepository.findAllByUsuario(user)
                .stream()
                .map(task -> mapper.map(task, TaskDTO.class))
                .toList();
    }
}
