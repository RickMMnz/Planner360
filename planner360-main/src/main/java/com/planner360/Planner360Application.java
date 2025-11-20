package com.planner360;

import com.planner360.model.Papel;
import com.planner360.model.Usuario;
import com.planner360.repository.PapelRepository;
import com.planner360.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = "com.planner360")
@EnableJpaRepositories(basePackages = "com.planner360.repository")
@EntityScan(basePackages = "com.planner360.model")
public class Planner360Application {

    public static void main(String[] args) {
        SpringApplication.run(Planner360Application.class, args);
    }

    // CommandLineRunner para criar o admin automaticamente
    @Bean
    public CommandLineRunner initAdmin(
            UsuarioRepository usuarioRepository,
            PapelRepository papelRepository,
            PasswordEncoder passwordEncoder) { // usa o bean do SecurityConfig

        return args -> {
            String adminEmail = "admin@planner360.com";

            if (usuarioRepository.findByEmail(adminEmail).isEmpty()) {
                // Verifica se o papel ADMIN existe
                Papel adminPapel = papelRepository.findByNome("ADMIN");
                if (adminPapel == null) {
                    adminPapel = new Papel();
                    adminPapel.setNome("ADMIN");
                    adminPapel = papelRepository.save(adminPapel);
                }

                // Cria o usuário admin
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setEmail(adminEmail);
                admin.setSenha(passwordEncoder.encode("admin123")); // senha padrão
                admin.setPapeis(List.of(adminPapel));

                usuarioRepository.save(admin);
                System.out.println("Usuário admin criado com sucesso!");
            } else {
                System.out.println("Usuário admin já existe.");
            }
        };
    }
}
