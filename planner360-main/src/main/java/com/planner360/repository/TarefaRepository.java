package com.planner360.repository;

import com.planner360.model.Tarefa;
import com.planner360.model.StatusTarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    // Buscar tarefas por múltiplos status
    List<Tarefa> findByStatusIn(List<StatusTarefa> statusList);

    // Buscar tarefas por status e usuário
    List<Tarefa> findByStatusAndUsuarioId(StatusTarefa status, Long usuarioId);

    // Buscar tarefas de um usuário específico
    List<Tarefa> findByUsuarioId(Long usuarioId);

    // Contar tarefas por status (para dashboard)
    long countByStatus(StatusTarefa status);

    // Contar tarefas por status e usuário (para dashboard personalizado)
    long countByStatusAndUsuarioId(StatusTarefa status, Long usuarioId);

    // Contar todas as tarefas de um usuário
    long countByUsuarioId(Long usuarioId);
}
