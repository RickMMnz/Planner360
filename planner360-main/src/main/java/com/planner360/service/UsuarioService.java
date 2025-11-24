package com.planner360.service;

import com.planner360.model.Papel;
import com.planner360.model.Usuario;
import com.planner360.repository.PapelRepository;
import com.planner360.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PapelRepository papelRepository;

    // ===== LISTAR =====
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    // ===== BUSCAR POR ID =====
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // ===== SALVAR (cadastro + edição) =====
    public Usuario salvar(Usuario usuario) {

        // Se o ID é nulo → cadastro novo
        if (usuario.getId() == null) {

            // Garante que ROLE_USER seja atribuído automaticamente
            Papel papelUser = papelRepository.findByNome("ROLE_USER");

            if (papelUser != null) {
                usuario.setPapeis(List.of(papelUser));
            }
        }

        return usuarioRepository.save(usuario);
    }

    // ===== DELETAR =====
    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }

    // ===== BUSCAR EMAIL =====
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    // ===== BUSCAR POR NOME =====
    public List<Usuario> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }
}
