package com.planner360.repository;

import com.planner360.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuário por e-mail (útil para login)
    Optional<Usuario> findByEmail(String email);

    // Verificar se e-mail já está cadastrado
    boolean existsByEmail(String email);

    // Buscar usuários por nome (útil para filtros ou compartilhamento)
    List<Usuario> findByNomeContainingIgnoreCase(String nome);
}