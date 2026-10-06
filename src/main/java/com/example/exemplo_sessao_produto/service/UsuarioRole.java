package com.example.exemplo_sessao_produto.service;

public enum UsuarioRole {
    ADMIN("admin"),
    USER("usuario");

    private String role;

    UsuarioRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
