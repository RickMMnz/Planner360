package com.planner360.config;

import com.planner360.model.Papel;
import com.planner360.model.Usuario;
import com.planner360.repository.PapelRepository;
import com.planner360.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
public class CommandLineRunnerConfig {

    @Bean
    CommandLineRunner initDatabase(PapelRepository papelRepo, UsuarioRepository usuarioRepo) {
        return args -> {

            // Lista de papéis necessários
            String[] nomesPapeis = {"ROLE_USER", "ROLE_ADMIN"};
            List<Papel> papeisSalvos = new ArrayList<>();

            // Cria os papéis caso não existam
            for (String nome : nomesPapeis) {
                Papel papel = papelRepo.findByNome(nome);
                if (papel == null) {
                    papel = new Papel(nome);
                    papelRepo.save(papel);
                    System.out.println("Papel criado: " + nome);
                }
                papeisSalvos.add(papel);
            }

            // --- Usuário admin original ---
            Optional<Usuario> adminExistente = usuarioRepo.findByEmail("admin@planner360.com");
            if (adminExistente.isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setEmail("admin@planner360.com");
                admin.setSenha(new BCryptPasswordEncoder().encode("123456"));
                admin.setPapeis(papeisSalvos);
                usuarioRepo.save(admin);
                System.out.println("Usuário admin criado com sucesso!");
            } else {
                System.out.println("Usuário admin já existe. Nenhuma ação realizada.");
            }

            // --- Segundo admin de teste ---
            Optional<Usuario> admin2Existente = usuarioRepo.findByEmail("admin2@planner360.com");
            if (admin2Existente.isEmpty()) {
                Usuario admin2 = new Usuario();
                admin2.setNome("Administrador 2");
                admin2.setEmail("admin2@planner360.com");
                admin2.setSenha(new BCryptPasswordEncoder().encode("654321")); // senha de teste
                admin2.setPapeis(papeisSalvos);
                usuarioRepo.save(admin2);
                System.out.println("Novo admin criado: admin2@planner360.com / senha: 654321");
            }
        };
    }
}
