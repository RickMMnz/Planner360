package com.planner360.config;

import com.planner360.security.MeuUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private MeuUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Rotas públicas
                .requestMatchers(
                    "/", 
                    "/app/usuarios/login",
                    "/app/usuarios/cadastro",
                    "/app/usuarios/salvar",
                    "/css/**",
                    "/js/**",
                    "/images/**"
                ).permitAll()
                // APIs abertas
                .requestMatchers("/api/**").permitAll()
                // Rotas que exigem autenticação
                .requestMatchers("/tarefas/**").authenticated()
                // Qualquer outra rota requer autenticação
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/app/usuarios/login")          // Página de login customizada
                .loginProcessingUrl("/app/usuarios/login") // POST do login
                .defaultSuccessUrl("/tarefas", true)       // Redireciona após login
                .usernameParameter("email")                // Campo de login
                .passwordParameter("senha")                // Campo de senha
                .permitAll()
            )
            .logout(logout -> logout
                // Configura logout via POST
                .logoutUrl("/logout")                     // URL que processa o POST
                .logoutSuccessUrl("/app/usuarios/login?logout") // Redireciona após logout
                .invalidateHttpSession(true)              // Limpa sessão
                .deleteCookies("JSESSIONID")              // Remove cookie de sessão
                .permitAll()
            )
            .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}