package com.example.exemplo_sessao_produto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.exemplo_sessao_produto.model.Usuario;
import com.example.exemplo_sessao_produto.repository.UsuarioDAO;

@Controller
public class UsuarioController {

    @Autowired
    UsuarioDAO repositorio;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String formLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String formCadastrarUsuario() {
        return "formCadastrarUsuario";
    }

    @PostMapping("/register")
    public String cadastrar(@RequestParam String username, @RequestParam String password, Model model) {
        if (username.isBlank() || password.isBlank()) {
            model.addAttribute("erroUsuario", "Informe o login e a senha!");
        } else if (repositorio.findByUsername(username) == null) {
            Usuario usuario = new Usuario();
            usuario.setUsername(username);
            usuario.setPassword(passwordEncoder.encode(password));
            repositorio.save(usuario);
            System.out.println("Usuário " + username + " criado com sucesso!");
            model.addAttribute("mensagemSucesso", "Usuário criado com sucesso!");
        } else {
            model.addAttribute("erroUsuario", "Já existe um usuário com esse nome!");
        }
        return "formCadastrarUsuario";
    }
}
