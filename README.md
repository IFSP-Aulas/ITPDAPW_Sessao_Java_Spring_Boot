# Exemplo de Sessão com Spring Boot

Exemplo da disciplina ITPDAPW (IFSP) sobre controle de sessão e autenticação com Spring Security, usando um cadastro de produtos.

## Funcionalidades

- Cadastro de usuários e login com Spring Security (senhas criptografadas com BCrypt)
- Expiração da sessão após 30 minutos sem uso
- Cadastro e listagem de produtos, com upload de logo
- Exclusão de produtos restrita a usuários com perfil `ADMIN`

## Tecnologias

- Java 21
- Spring Boot 3.5 (Web, Data JPA, Security, Thymeleaf, DevTools)
- MariaDB / MySQL

## Como executar

1. Tenha o MariaDB (ou MySQL) rodando em `localhost:3306`.
2. Ajuste usuário e senha do banco em `src/main/resources/application.properties`. O banco `aula_sessao` é criado automaticamente.
3. Execute:

   ```bash
   mvn spring-boot:run
   ```

4. Acesse http://localhost:8080/register, cadastre um usuário e faça login em http://localhost:8080/login.

Para dar o perfil de administrador a um usuário:

```sql
UPDATE usuario SET role = 'ADMIN' WHERE username = 'seu_usuario';
```
