package com.br.todo.service;

import com.br.todo.dto.task.TaskCreateDTO;
import com.br.todo.dto.task.TaskDTO;
import com.br.todo.dto.task.TaskUpdateDTO;
import com.br.todo.entity.Task;
import com.br.todo.entity.User;
import com.br.todo.entity.enumeration.TaskStatus;
import com.br.todo.exception.RegraDeNegocioException;
import com.br.todo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    public List<TaskDTO> findAllByUserAndStatus(String authHeader, String taskStatus) {
        User user = userService.getLoggedUser(authHeader);
        List<Task> tasks;

        if(taskStatus.equalsIgnoreCase("F")) {
            tasks = taskRepository.findAllByUsuarioAndStatus(user, TaskStatus.F);
        } else if(taskStatus.equalsIgnoreCase("P")) {
            tasks = taskRepository.findAllByUsuarioAndStatus(user, TaskStatus.P);
        } else {
            throw new RegraDeNegocioException("Foi fornecido um valor de status para busca inexistente. Tente novamente com um valor válido.");
        }

        return tasks.stream()
                .map(task -> mapper.map(task, TaskDTO.class))
                .toList();
    }

    public List<TaskDTO> findAllByUserAndNomeTarefa(String authHeader, String nomeTarefa) {
        User user = userService.getLoggedUser(authHeader);

        return taskRepository.findAllByUsuarioAndNomeTarefaContainingIgnoreCase(user, nomeTarefa.trim())
                .stream()
                .map(task -> mapper.map(task, TaskDTO.class))
                .toList();
    }

    public TaskDTO create(TaskCreateDTO taskCreateDTO, String authHeader) throws IOException {
        User user = userService.getLoggedUser(authHeader);

        Task novaTarefa = new Task();
        novaTarefa.setNomeTarefa(taskCreateDTO.getNomeTask());
        novaTarefa.setDescricao(taskCreateDTO.getDescricao());
        novaTarefa.setUsuario(user);
        novaTarefa.setStatus(TaskStatus.P);

        Task tarefaCriada = taskRepository.save(novaTarefa);
        return mapper.map(tarefaCriada, TaskDTO.class);
    }

    public TaskDTO updateTask(Integer idTask, TaskUpdateDTO taskUpdateDTO, String authHeader) {
        Task tarefa = getTask(idTask);

        validateUserIsTheSame(tarefa, authHeader, "editar");

        if(taskUpdateDTO.getNomeTask() != null) tarefa.setNomeTarefa(taskUpdateDTO.getNomeTask());
        if(taskUpdateDTO.getDescricao() != null) tarefa.setDescricao(taskUpdateDTO.getDescricao());

        return mapper.map(taskRepository.save(tarefa), TaskDTO.class);
    }

    public void deleteTask(Integer idTask, String authHeader) {
        Task tarefa = getTask(idTask);
        validateUserIsTheSame(tarefa, authHeader, "deletar");
        taskRepository.delete(tarefa);
    }

    public Task getTask(Integer idTask) {
        return taskRepository.findById(idTask)
                .orElseThrow(() -> new RegraDeNegocioException("Nenhuma tarefa com id " + idTask + " encontrada."));
    }

    public void markTaskAsComplete(Integer idTask, String authHeader) {
         Task task = getTask(idTask);
         validateUserIsTheSame(task, authHeader, "completar");
         task.setStatus(TaskStatus.F);
         taskRepository.save(task);
    }

    public void markTaskAsUncomplete(Integer idTask, String authHeader) {
        Task task = getTask(idTask);
        validateUserIsTheSame(task, authHeader, "descompletar");
        task.setStatus(TaskStatus.P);
        taskRepository.save(task);
    }

    private void validateUserIsTheSame(Task tarefa, String authHeader, String action) {
        User userSolicitante = userService.getLoggedUser(authHeader);
        if(!userSolicitante.getIdUsuario().equals(tarefa.getUsuario().getIdUsuario())) {
            throw new RegraDeNegocioException("Você não tem permissão para " + action + " esta tarefa.");
        }
    }
}
