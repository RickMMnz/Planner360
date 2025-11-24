package com.planner360.controller;

import com.planner360.model.Tarefa;
import com.planner360.model.Usuario;
import com.planner360.model.StatusTarefa;
import com.planner360.repository.PapelRepository;
import com.planner360.service.TarefaService;
import com.planner360.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/app/usuarios")
public class UsuarioWebController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PapelRepository papelRepository;

    @Autowired
    private TarefaService tarefaService;

    // Página de login
    @GetMapping("/login")
    public String loginPage() {
        return "usuarios/login";
    }

    // Página de perfil do usuário
    @GetMapping("/perfil")
    public String perfil(@AuthenticationPrincipal User userDetails, Model model) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername())
                .orElse(new Usuario());

        List<Tarefa> tarefasPendentes = tarefaService.listarPorUsuario(usuario.getId())
                .stream()
                .filter(t -> t.getStatus() == StatusTarefa.PENDENTE)
                .collect(Collectors.toList());

        model.addAttribute("usuario", usuario);
        model.addAttribute("pendentes", tarefasPendentes);

        return "usuarios/perfil";
    }

    // Formulário de cadastro
    @GetMapping("/cadastro")
    public String cadastroForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/cadastro";
    }

    // Salvar novo usuário
    @PostMapping("/salvar")
    public String salvarUsuario(@ModelAttribute Usuario usuario, Model model) {
        usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
        usuario.setPapeis(List.of(papelRepository.findByNome("ROLE_USER")));
        usuarioService.salvar(usuario);

        return "redirect:/app/usuarios/login";
    }

    // ================= Métodos para usuário padrão =================

    // Lista de tarefas do usuário logado
    @GetMapping("/tarefas")
    public String listarTarefas(@AuthenticationPrincipal User userDetails, Model model) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername())
                .orElse(new Usuario());

        List<Tarefa> tarefas = tarefaService.listarPorUsuario(usuario.getId());
        model.addAttribute("tarefas", tarefas);
        model.addAttribute("usuario", usuario);

        return "tarefas/lista";
    }

    // Dashboard do usuário logado
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal User userDetails, Model model) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername())
                .orElse(new Usuario());

        model.addAttribute("total", tarefaService.contarTotalPorUsuario(usuario.getId()));
        model.addAttribute("pendentes", tarefaService.contarPendentesPorUsuario(usuario.getId()));
        model.addAttribute("emAndamento", tarefaService.contarEmAndamentoPorUsuario(usuario.getId()));
        model.addAttribute("concluidas", tarefaService.contarConcluidasPorUsuario(usuario.getId()));
        model.addAttribute("usuario", usuario);

        return "tarefas/dashboard";
    }
}
