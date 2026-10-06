package com.example.exemplo_sessao_produto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.exemplo_sessao_produto.model.Usuario;

public interface UsuarioDAO extends JpaRepository<Usuario, Long> {
    // O Spring Data JPA cria a consulta automaticamente a partir do nome do método
    UserDetails findByUsername(String username);
}
