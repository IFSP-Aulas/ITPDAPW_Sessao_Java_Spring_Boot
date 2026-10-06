package com.example.exemplo_sessao_produto.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Anotações para indicar que as configurações padrões do WebSecurity serão ajustadas
// nesta classe
@Configuration
@EnableWebSecurity
public class ConfiguracoesSeguranca {

    // Anotação de Bean para o Spring instanciar, configurar e gerenciar o objeto (IoC)
    @Bean
    public SecurityFilterChain correnteFiltroSeguranca(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // CSRF para desligar essa configuração
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso público ao cadastro de usuário, ao login e aos arquivos de estilo
                        .requestMatchers(HttpMethod.GET, "/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/register").permitAll()
                        .requestMatchers("/login", "/css/**", "/error").permitAll()
                        // Somente administradores podem excluir produtos
                        .requestMatchers("/excluirProduto/**").hasRole("ADMIN")
                        .anyRequest().authenticated() // Exige autenticação para qualquer outra requisição
                )
                .formLogin(form -> form.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/menu", true)
                        .failureUrl("/login?erro")
                        .permitAll()) // Habilita o formulário de login
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?sair")
                        .invalidateHttpSession(true) // Destrói a sessão no servidor
                        .deleteCookies("JSESSIONID") // Remove o cookie da sessão no navegador
                        .permitAll())
                // Gerenciamento da sessão:
                // SessionCreationPolicy.STATELESS (sem sessão, via token) é indicado para APIs REST.
                // Como aqui o login é feito por formulário, o usuário autenticado precisa ficar
                // guardado na sessão (cookie JSESSIONID), por isso usamos IF_REQUIRED.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .sessionFixation(fixation -> fixation.migrateSession()) // Novo ID de sessão após o login
                        .invalidSessionUrl("/login?expirou")
                        .maximumSessions(1)) // Um login ativo por usuário
                // criar objeto SecurityFilterChain para retornar no método
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Define o codificador de senhas que será usado na aplicação
        return new BCryptPasswordEncoder();
    }
}
