package com.planner360.security;

import com.planner360.model.Usuario;
import com.planner360.model.Papel;
import com.planner360.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // Torna essa classe um serviço gerenciado
public class MeuUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        List<SimpleGrantedAuthority> authorities = usuario.getPapeis().stream()
            .map(papel -> new SimpleGrantedAuthority(papel.getNome()))
            .collect(Collectors.toList());

        return new User(usuario.getEmail(), usuario.getSenha(), authorities);
    }
}
