package com.planner360.service;

import com.planner360.model.Usuario;
import com.planner360.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService{
    
    @Autowired //Injeta automaticamente o repositório
    private UsuarioRepository usuarioRepository;

    //Listar todos os usuários
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    //Buscar Usuário por ID
    public Optional<Usuario> buscarPorId(Long id){
        return usuarioRepository.findById(id);
    }

    //Salvar ou atualizar usuário
    public Usuario salvar(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    //Deletar usuário por ID
    public void deletar(Long id){
        usuarioRepository.deleteById(id);
    }

    //Buscar se o e-mail já está cadastrado
    public Optional<Usuario> buscarPorEmail(String email){
        return usuarioRepository.findByEmail(email);
    }

    //Verifica se o  e-mail já está cadastrado 
    public boolean emailExiste(String email){
        return usuarioRepository.existsByEmail(email);
    }

    //Buscar usuários por nome
    public List<Usuario> buscarPorNome(String nome){
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }

}