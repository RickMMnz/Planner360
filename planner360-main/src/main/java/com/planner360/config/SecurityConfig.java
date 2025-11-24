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
            // ===== AUTORIZAÇÃO DE REQUISIÇÕES =====
            .authorizeHttpRequests(auth -> auth
                // URLs públicas
                .requestMatchers(
                    "/",
                    "/app/usuarios/login",
                    "/app/usuarios/cadastro",
                    "/app/usuarios/salvar",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/api/**"
                ).permitAll()

                // URLs restritas
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Admins também podem acessar dashboards e tarefas
                .requestMatchers("/tarefas/**", "/app/usuarios/**").hasAnyRole("USER","ADMIN")

                // Qualquer outra requisição precisa de autenticação
                .anyRequest().authenticated()
            )

            // ===== LOGIN =====
            .formLogin(form -> form
                .loginPage("/app/usuarios/login")
                .loginProcessingUrl("/app/usuarios/login")
                .usernameParameter("email")
                .passwordParameter("senha")
                .successHandler((request, response, authentication) -> {

                    // Log de debug das roles
                    System.out.println("Usuário autenticado: " + authentication.getName());
                    authentication.getAuthorities()
                        .forEach(a -> System.out.println("Authority: " + a.getAuthority()));

                    // Redirecionamento baseado em role
                    boolean isAdmin = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
                    boolean isUser = authentication.getAuthorities().stream()
                            .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));

                    if (isAdmin) response.sendRedirect("/admin/usuarios");
                    else if (isUser) response.sendRedirect("/app/usuarios/dashboard");
                    else response.sendRedirect("/app/usuarios/login");
                })
                .permitAll()
            )

            // ===== LOGOUT =====
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/app/usuarios/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )

            // Define UserDetailsService explicitamente
            .userDetailsService(userDetailsService)

            // CSRF ativado por padrão, apenas descomente se necessário
            // .csrf(csrf -> csrf.disable())
            ;

        return http.build();
    }

    // ===== CODIFICADOR DE SENHA =====
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}