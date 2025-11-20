package com.planner360.controller;

import com.planner360.model.Tarefa;
import com.planner360.service.TarefaService;
import com.planner360.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tarefas") // Todas as rotas começam com /tarefas
public class TarefaWebController {

    @Autowired
    private TarefaService tarefaService; // Serviço que manipula tarefas

    @Autowired
    private UsuarioService usuarioService; // Serviço que manipula usuários

    // Lista todas as tarefas do usuário logado
    @GetMapping
    public String listarTarefas(@AuthenticationPrincipal User userDetails, Model model) {
        // Pega o ID do usuário logado pelo email
        Long usuarioId = usuarioService.buscarPorEmail(userDetails.getUsername())
                .map(u -> u.getId())
                .orElse(0L);

        // Busca todas as tarefas desse usuário
        List<Tarefa> tarefas = tarefaService.listarPorUsuario(usuarioId);
        model.addAttribute("tarefas", tarefas); // Passa para o Thymeleaf

        // Adiciona o usuário logado ao model (para dropdown de perfil/logout)
        usuarioService.buscarPorEmail(userDetails.getUsername())
                .ifPresent(u -> model.addAttribute("usuario", u));

        return "tarefas/lista"; // Renderiza lista.html
    }

    // Formulário para criar nova tarefa
    @GetMapping("/nova")
    public String novaTarefa(Model model) {
        model.addAttribute("tarefa", new Tarefa()); // Cria uma tarefa vazia
        return "tarefas/form"; // Renderiza o formulário
    }

    // Formulário para editar tarefa existente
    @GetMapping("/editar/{id}")
    public String editarTarefa(@PathVariable Long id, Model model) {
        Tarefa tarefa = tarefaService.buscarPorId(id).orElse(null);
        if (tarefa == null) return "redirect:/tarefas"; // Se não existe, redireciona

        model.addAttribute("tarefa", tarefa); // Passa tarefa existente para o formulário
        return "tarefas/form";
    }

    // Salva ou atualiza tarefa
    @PostMapping("/salvar")
    public String salvarTarefa(@ModelAttribute Tarefa tarefa, @AuthenticationPrincipal User userDetails) {
        // Define o usuário logado como dono da tarefa
        usuarioService.buscarPorEmail(userDetails.getUsername()).ifPresent(u -> tarefa.setUsuario(u));
        tarefaService.salvar(tarefa); // Salva no banco
        return "redirect:/tarefas"; // Redireciona para a lista
    }

    // Exclui uma tarefa pelo ID
    @GetMapping("/excluir/{id}")
    public String excluirTarefa(@PathVariable Long id) {
        tarefaService.deletar(id);
        return "redirect:/tarefas"; // Redireciona para a lista
    }

    // Dashboard do usuário (quantidade de tarefas por status)
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal User userDetails, Model model) {
        Long usuarioId = usuarioService.buscarPorEmail(userDetails.getUsername())
                .map(u -> u.getId())
                .orElse(0L);

        model.addAttribute("total", tarefaService.contarTotalPorUsuario(usuarioId));
        model.addAttribute("pendentes", tarefaService.contarPendentesPorUsuario(usuarioId));
        model.addAttribute("emAndamento", tarefaService.contarEmAndamentoPorUsuario(usuarioId));
        model.addAttribute("concluidas", tarefaService.contarConcluidasPorUsuario(usuarioId));

        return "tarefas/dashboard"; // Renderiza dashboard.html
    }
}