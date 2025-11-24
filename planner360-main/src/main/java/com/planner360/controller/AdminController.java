package com.planner360.controller;

import com.planner360.model.Papel;
import com.planner360.model.Usuario;
import com.planner360.repository.PapelRepository;
import com.planner360.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PapelRepository papelRepository;

    // ===== LISTA DE USUÁRIOS =====
@GetMapping
public String listarUsuarios(Model model) {
    System.out.println("AdminController: listarUsuarios() chamado!");
    List<Usuario> usuarios = usuarioService.listarTodos();
    if (usuarios == null || usuarios.isEmpty()) {
        System.out.println("Nenhum usuário encontrado.");
    } else {
        System.out.println("Usuarios encontrados: " + usuarios.size());
    }
    model.addAttribute("usuarios", usuarios);
    return "admin/admin-lista";
}


    // ===== FORMULÁRIO DE EDIÇÃO =====
    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário inválido: " + id));

        List<Papel> papeis = papelRepository.findAll();

        model.addAttribute("usuario", usuario);
        model.addAttribute("papeis", papeis);

        return "admin/admin-editar"; // <<< CAMINHO CORRETO DO TEMPLATE
    }

    // ===== SALVAR =====
    @PostMapping("/editar/{id}")
    public String atualizarUsuario(
            @PathVariable Long id,
            @RequestParam(value = "papeis", required = false) List<Long> papeisIds) {

        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário inválido: " + id));

        Set<Papel> novosPapeis = new HashSet<>();

        if (papeisIds != null) {
            novosPapeis.addAll(papelRepository.findAllById(papeisIds));
        }

        usuario.setPapeis(novosPapeis.stream().toList());
        usuarioService.salvar(usuario);

        return "redirect:/admin/usuarios";
    }

    // ===== DELETAR =====
    @GetMapping("/deletar/{id}")
    public String deletarUsuario(@PathVariable Long id) {
        usuarioService.deletar(id);
        return "redirect:/admin/usuarios";
    }
}