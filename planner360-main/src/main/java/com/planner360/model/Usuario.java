package com.planner360.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*; // Importa anotações de validação
import java.util.List;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "O nome é obrigatório") // Não permite nome vazio
    @Size(min = 3, max = 50, message = "O nome deve ter entre 3 e 50 caracteres") // Limite de caracteres
    private String nome;

    @NotBlank(message = "O email é obrigatório") // Não permite email vazio
    @Email(message = "O email deve ser válido") // Valida formato do email
    private String email;

    @NotBlank(message = "A senha é obrigatória") // Não permite senha vazia
    @Size(min = 6, message = "A senha deve ter ao menos 6 caracteres") // Tamanho mínimo
    private String senha;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_papel",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "papel_id")
    )
    private List<Papel> papeis;

    @OneToMany(mappedBy = "usuario")
    private List<Tarefa> tarefas;

    // Getters e setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public List<Papel> getPapeis() { return papeis; }
    public void setPapeis(List<Papel> papeis) { this.papeis = papeis; }

    public List<Tarefa> getTarefas() { return tarefas; }
    public void setTarefas(List<Tarefa> tarefas) { this.tarefas = tarefas; }
}
