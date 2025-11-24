package com.planner360.controller;

import com.planner360.model.Tarefa;
import com.planner360.model.Usuario;
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
@RequestMapping("/tarefas")
public class TarefaWebController {

    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private UsuarioService usuarioService;

    // Lista todas as tarefas do usuário logado
    @GetMapping
    public String listarTarefas(@AuthenticationPrincipal User userDetails, Model model) {
        if (userDetails == null) {
            model.addAttribute("error", "Usuário não autenticado. Faça login para acessar suas tarefas.");
            return "usuarios/login"; // Redireciona para login
        }

        Long usuarioId = usuarioService.buscarPorEmail(userDetails.getUsername())
                .map(Usuario::getId)
                .orElse(null);

        if (usuarioId == null) {
            model.addAttribute("error", "Usuário não encontrado no sistema.");
            return "usuarios/login";
        }

        List<Tarefa> tarefas = tarefaService.listarPorUsuario(usuarioId);
        model.addAttribute("tarefas", tarefas);

        usuarioService.buscarPorEmail(userDetails.getUsername())
                .ifPresent(u -> model.addAttribute("usuario", u));

        return "tarefas/lista";
    }

    // Formulário para criar nova tarefa
    @GetMapping("/nova")
    public String novaTarefa(@AuthenticationPrincipal User userDetails, Model model) {
        if (userDetails == null) return "redirect:/app/usuarios/login";

        model.addAttribute("tarefa", new Tarefa());
        return "tarefas/form";
    }

    // Formulário para editar tarefa existente
    @GetMapping("/editar/{id}")
    public String editarTarefa(@PathVariable Long id, @AuthenticationPrincipal User userDetails, Model model) {
        if (userDetails == null) return "redirect:/app/usuarios/login";

        Tarefa tarefa = tarefaService.buscarPorId(id).orElse(null);
        if (tarefa == null) return "redirect:/tarefas";

        model.addAttribute("tarefa", tarefa);
        return "tarefas/form";
    }

    // Salva ou atualiza tarefa
    @PostMapping("/salvar")
    public String salvarTarefa(@ModelAttribute Tarefa tarefa, @AuthenticationPrincipal User userDetails) {
        if (userDetails == null) return "redirect:/app/usuarios/login";

        usuarioService.buscarPorEmail(userDetails.getUsername())
                .ifPresent(tarefa::setUsuario);

        tarefaService.salvar(tarefa);
        return "redirect:/tarefas";
    }

    // Exclui uma tarefa pelo ID
    @GetMapping("/excluir/{id}")
    public String excluirTarefa(@PathVariable Long id, @AuthenticationPrincipal User userDetails) {
        if (userDetails == null) return "redirect:/app/usuarios/login";

        tarefaService.deletar(id);
        return "redirect:/tarefas";
    }

    // Dashboard do usuário
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal User userDetails, Model model) {
        if (userDetails == null) return "redirect:/app/usuarios/login";

        Long usuarioId = usuarioService.buscarPorEmail(userDetails.getUsername())
                .map(u -> u.getId())
                .orElse(0L);

        model.addAttribute("total", tarefaService.contarTotalPorUsuario(usuarioId));
        model.addAttribute("pendentes", tarefaService.contarPendentesPorUsuario(usuarioId));
        model.addAttribute("emAndamento", tarefaService.contarEmAndamentoPorUsuario(usuarioId));
        model.addAttribute("concluidas", tarefaService.contarConcluidasPorUsuario(usuarioId));

        return "tarefas/dashboard";
    }
}
