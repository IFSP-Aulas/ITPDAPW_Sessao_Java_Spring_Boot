package com.example.exemplo_sessao_produto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.exemplo_sessao_produto.repository.UsuarioDAO;

@Service
public class AutorizacaoService implements UserDetailsService {

    @Autowired
    UsuarioDAO usuarioDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails usuario = usuarioDao.findByUsername(username);
        // O Spring Security exige que o método nunca retorne null
        if (usuario == null)
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        return usuario;
    }
}
