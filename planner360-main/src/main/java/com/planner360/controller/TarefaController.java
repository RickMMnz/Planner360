package com.planner360.controller;

import com.planner360.model.Tarefa;
import com.planner360.service.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;

    // Lista todas as tarefas
    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTodas() {
        List<Tarefa> tarefas = tarefaService.listarTodas();
        return ResponseEntity.ok(tarefas);
    }

    // Busca uma tarefa por ID
    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarPorId(@PathVariable Long id) {
        return tarefaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Salva ou Atualiza uma tarefa
    @PostMapping
    public ResponseEntity<Tarefa> salvar(@RequestBody Tarefa tarefa) {
        Tarefa tarefaSalva = tarefaService.salvar(tarefa);
        return ResponseEntity.ok(tarefaSalva);
    }

    // Deleta uma tarefa pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable long id) {
        if (tarefaService.buscarPorId(id).isPresent()) {
            tarefaService.deletar(id);
            return ResponseEntity.noContent().build(); // HTTP 204
        }
        return ResponseEntity.notFound().build();
    }

    // Lista tarefas com Status PENDENTE ou EM_ANDAMENTO
    @GetMapping("/ativas")
    public ResponseEntity<List<Tarefa>> listarAtivas() {
        List<Tarefa> tarefasAtivas = tarefaService.listarAtivas();
        return ResponseEntity.ok(tarefasAtivas);
    }

    // Retorna um resumo com contagem de tarefas por status - para Dashboard
    @GetMapping("/dashboard/{usuarioId}")
    public ResponseEntity<Map<String, Long>> dashboard(@PathVariable Long usuarioId) {
        Map<String, Long> dados = new HashMap<>();
        dados.put("total", tarefaService.contarTotalPorUsuario(usuarioId));
        dados.put("pendentes", tarefaService.contarPendentesPorUsuario(usuarioId));
        dados.put("emAndamento", tarefaService.contarEmAndamentoPorUsuario(usuarioId));
        dados.put("concluidas", tarefaService.contarConcluidasPorUsuario(usuarioId));
        return ResponseEntity.ok(dados);
    }
}
