package com.br.todo.repository;

import com.br.todo.entity.Task;
import com.br.todo.entity.User;
import com.br.todo.entity.enumeration.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllByUsuario(User usuario);

    List<Task> findAllByUsuarioAndStatus(User usuario, TaskStatus taskStatus);

    List<Task> findAllByUsuarioAndNomeTarefaContainingIgnoreCase(User user, String nomeTarefa);
}
