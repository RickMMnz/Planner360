package com.planner360.security;

import com.planner360.model.Usuario;
import com.planner360.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeuUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        // Transformar papéis em autoridades do Spring Security
        List<SimpleGrantedAuthority> authorities = usuario.getPapeis().stream()
            .map(papel -> new SimpleGrantedAuthority(papel.getNome()))
            .collect(Collectors.toList());

        // Log detalhado para debug
        System.out.println("Usuario encontrado: " + usuario.getEmail());
        System.out.println("Roles atribuídas:");
        authorities.forEach(a -> System.out.println("  - " + a.getAuthority()));

        return new User(usuario.getEmail(), usuario.getSenha(), authorities);
    }
}