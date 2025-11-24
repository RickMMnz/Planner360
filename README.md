# Planner360

## 1. Sobre o Projeto
**Planner360** é um sistema web de gerenciamento de tarefas, desenvolvido para ajudar usuários a organizar suas atividades pessoais, de trabalho e estudo.  
O sistema permite cadastro de usuários, login seguro, criação, edição e acompanhamento de tarefas, além de uma dashboard com status das tarefas.  
O público-alvo são pessoas que buscam melhorar sua produtividade e organização diária.

## 2. Tecnologias Utilizadas
- **Backend:** Java 17+, Spring Boot, Spring Web, Spring Data JPA, Spring Security, Bean Validation
- **Frontend:** Thymeleaf, HTML, CSS
- **Banco de Dados:** MySQL 8+
- **Build:** Maven

## 3. Arquitetura
- Camadas: `Controller` → `Service` → `Repository`
- Segurança: autenticação por formulário, perfis `ROLE_USER` e `ROLE_ADMIN`
- Banco de dados relacional (MySQL) com JPA/Hibernate

## 4. Requisitos de Ambiente
- Java JDK 17+
- Maven 3.8+
- MySQL 8+

## 5. Configuração da Aplicação

Nome da aplicação
spring.application.name=planner360

Configuração do banco de dados MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/planner360?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=S3nh@n0vA7456
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

Configuração do JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

Mostrar SQL gerado no console
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

Usar UTF-8 para evitar problemas com acentuação
spring.datasource.hikari.connection-init-sql=SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci

(Opcional) Configuração de logs para depuração
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

## 6. Instalação e Execução
git clone
cd planner360-main
mvn spring-boot:run
#Após a instalação, acesse a aplicação em: http://localhost:8080

## 8. Seed de Usuários
Foi utilizado o commandlinerunner com BCryptPasswordEncoder, para criar as Roles de admin e user, sendo necessário a inserção de usuário, e-mail e a senha (depois o hash vai criptografar).

## 9. Segurança
Públicas (acesso sem login):
/
/app/usuarios/login
/app/usuarios/cadastro
/app/usuarios/salvar
/css/**
/js/**
/images/**
/api/**

Autenticadas (USER ou ADMIN):
/app/usuarios/perfil
/app/usuarios/dashboard
/app/usuarios/tarefas
/tarefas/**

Apenas ADMIN:
/admin/**

Login: formulário em /app/usuarios/login

Logout: /logout (sessão invalidada, redireciona para login)

## 10> Rotas Web

Públicas:
GET    /                       - Página inicial
GET    /app/usuarios/login      - Página de login
POST   /app/usuarios/login      - Processa login
GET    /app/usuarios/cadastro   - Formulário de cadastro
POST   /app/usuarios/salvar     - Salva novo usuário

Autenticadas (USER ou ADMIN):
GET    /app/usuarios/perfil     - Perfil do usuário logado
GET    /app/usuarios/dashboard  - Dashboard do usuário logado
GET    /app/usuarios/tarefas    - Lista tarefas do usuário logado
GET    /tarefas                  - Lista tarefas do usuário logado
GET    /tarefas/nova             - Formulário para criar nova tarefa
GET    /tarefas/editar/{id}      - Formulário para editar tarefa existente
POST   /tarefas/salvar           - Salva ou atualiza tarefa
GET    /tarefas/excluir/{id}     - Exclui uma tarefa
GET    /tarefas/dashboard        - Dashboard de tarefas do usuário

Apenas ADMIN:
GET    /admin/usuarios           - Lista usuários
GET    /admin/usuarios/editar/{id} - Formulário para editar usuário
POST   /admin/usuarios/editar/{id} - Atualiza usuário
GET    /admin/usuarios/deletar/{id} - Deleta usuário

Observações de login/logout:
- Login: /app/usuarios/login
- Logout: /logout (redireciona para login, sessão encerrada)


##11. API Endpoints (/api/**)
---------------------------

Usuários:
GET    /api/usuarios                     - Lista todos os usuários
GET    /api/usuarios/{id}                - Busca usuário pelo ID
GET    /api/usuarios/email/{email}       - Busca usuário pelo e-mail
GET    /api/usuarios/existe/{email}      - Verifica se o e-mail existe
GET    /api/usuarios/buscar?nome=xxx    - Busca usuários pelo nome (parcial)
POST   /api/usuarios                     - Salva ou atualiza usuário (JSON body)
DELETE /api/usuarios/{id}                - Deleta usuário pelo ID

Tarefas:
GET    /api/tarefas                     - Lista todas as tarefas
GET    /api/tarefas/{id}                - Busca tarefa pelo ID
POST   /api/tarefas                     - Salva ou atualiza tarefa (JSON body)
DELETE /api/tarefas/{id}                - Deleta tarefa pelo ID
GET    /api/tarefas/ativas              - Lista tarefas pendentes ou em andamento
GET    /api/tarefas/dashboard/{usuarioId} - Contagem de tarefas por status para dashboard





