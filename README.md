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

## 5. Configuração do Banco


## 6. Configuração da Aplicação

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

## 7. Instalação e Execução
git clone
cd planner360
mvn spring-boot:run
#Após a instalação, acesse a aplicação em: http://localhost:8080

## 8. Seed de Usuários
Foi utilizado o comamandlinerunner com BCyptPasswordEncoder, para criar as Roles de Admmin e user, sendo necessário a inserção de usuário, e-mail e a senha (depois o hash vai criptografar).

## 9. Rotas e Segurança


## 10> APE REST


