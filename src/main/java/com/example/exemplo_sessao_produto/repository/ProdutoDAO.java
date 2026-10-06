package com.example.exemplo_sessao_produto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.exemplo_sessao_produto.model.Produto;

public interface ProdutoDAO extends JpaRepository<Produto, Long> {
}
