package com.planner360.service;

import com.planner360.model.Tarefa;
import com.planner360.model.StatusTarefa;
import com.planner360.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Arrays;
import java.util.Optional;

@Service
public class TarefaService {

    @Autowired
    private TarefaRepository tarefaRepository;

    // Buscar todas as tarefas
    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }

    // Buscar tarefas por usuário
    public List<Tarefa> listarPorUsuario(Long usuarioId) {
        return tarefaRepository.findByUsuarioId(usuarioId);
    }

    // Buscar tarefa por ID
    public Optional<Tarefa> buscarPorId(Long id) {
        return tarefaRepository.findById(id);
    }

    // Salvar ou atualizar tarefa
    public Tarefa salvar(Tarefa tarefa) {
        return tarefaRepository.save(tarefa);
    }

    // Deletar tarefa por ID
    public void deletar(Long id) {
        tarefaRepository.deleteById(id);
    }

    // Buscar tarefas com status PENDENTE ou EM_ANDAMENTO
    public List<Tarefa> listarAtivas() {
        return tarefaRepository.findByStatusIn(Arrays.asList(StatusTarefa.PENDENTE, StatusTarefa.EM_ANDAMENTO));
    }

    // Contagem de tarefas por status (para dashboard)
    public long contarConcluidasPorUsuario(Long usuarioId) {
        return tarefaRepository.countByStatusAndUsuarioId(StatusTarefa.CONCLUIDA, usuarioId);
    }

    public long contarPendentesPorUsuario(Long usuarioId) {
        return tarefaRepository.countByStatusAndUsuarioId(StatusTarefa.PENDENTE, usuarioId);
    }

    public long contarEmAndamentoPorUsuario(Long usuarioId) {
        return tarefaRepository.countByStatusAndUsuarioId(StatusTarefa.EM_ANDAMENTO, usuarioId);
    }

    public long contarTotalPorUsuario(Long usuarioId) {
        return tarefaRepository.countByUsuarioId(usuarioId);
    }
}
