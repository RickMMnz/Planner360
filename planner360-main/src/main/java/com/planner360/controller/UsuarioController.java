package com.planner360.controller;

import com.planner360.model.Usuario;
import com.planner360.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

// Indica que este é um controller REST que retorna JSON
@RestController
// Define o prefixo das rotas: todas começam com /api/usuarios
@RequestMapping("/api/usuarios")
public class UsuarioController {

    // Injeta o serviço de usuário para acessar a lógica de negócio
    @Autowired
    private UsuarioService usuarioService;

    // Lista todos os usuários cadastrados
    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioService.listarTodos();
    }

    // Busca um usuário pelo ID
    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id).orElse(null);
    }

    // Busca um usuário pelo e-mail 
    @GetMapping("/email/{email}")
    public Optional<Usuario> buscarPorEmail(@PathVariable String email) {
        return usuarioService.buscarPorEmail(email);
    }

    // Verifica se o e-mail já está cadastrado 
    @GetMapping("/existe/{email}")
    public boolean emailExiste(@PathVariable String email) {
        return usuarioService.emailExiste(email);
    }

    // Busca usuários cujo nome contenha o termo informado 
    @GetMapping("/buscar")
    public List<Usuario> buscarPorNome(@RequestParam String nome) {
        return usuarioService.buscarPorNome(nome);
    }

    // Salva ou atualiza um usuário (recebe JSON no corpo da requisição)
    @PostMapping
    public Usuario salvar(@RequestBody Usuario usuario) {
        return usuarioService.salvar(usuario);
    }

    // Deleta um usuário pelo ID
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
    }
}
