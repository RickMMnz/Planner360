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
import org.springframework.validation.BindingResult; // Para capturar erros de validação
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid; // Importa anotação @Valid
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

    // Salvar novo usuário com validação
    @PostMapping("/salvar")
    public String salvarUsuario(@Valid @ModelAttribute Usuario usuario, BindingResult result, Model model) {
        // Se houver erros de validação, retorna para o formulário
        if (result.hasErrors()) {
            return "usuarios/cadastro"; // Mantém os erros no formulário
        }

        // Criptografa a senha
        usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));

        // Define papel padrão
        usuario.setPapeis(List.of(papelRepository.findByNome("ROLE_USER")));

        // Salva usuário
        usuarioService.salvar(usuario);

        // Redireciona para login
        return "redirect:/app/usuarios/login";
    }
}