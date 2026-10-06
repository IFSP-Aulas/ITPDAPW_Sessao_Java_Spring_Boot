package com.example.exemplo_sessao_produto;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ExemploSessaoProdutoApplicationTests {

	@Autowired
	MockMvc mvc;

	@Test
	void paginaProtegidaRedirecionaParaLogin() throws Exception {
		mvc.perform(get("/menu")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrlPattern("**/login"));
	}

	@Test
	void cadastroDeUsuarioELoginFuncionam() throws Exception {
		mvc.perform(post("/register").param("username", "maria").param("password", "123"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("mensagemSucesso"));

		// Cadastrar de novo o mesmo login deve gerar erro
		mvc.perform(post("/register").param("username", "maria").param("password", "123"))
				.andExpect(model().attributeExists("erroUsuario"));

		mvc.perform(formLogin("/login").user("maria").password("123"))
				.andExpect(authenticated().withUsername("maria").withRoles("USUARIO"))
				.andExpect(redirectedUrl("/menu"));

		mvc.perform(formLogin("/login").user("maria").password("errada"))
				.andExpect(unauthenticated())
				.andExpect(redirectedUrl("/login?erro"));
	}

	@Test
	void somenteAdminPodeExcluirProduto() throws Exception {
		mvc.perform(post("/excluirProduto/1").with(user("joao").roles("USUARIO")))
				.andExpect(status().isForbidden());
		mvc.perform(post("/excluirProduto/1").with(user("admin").roles("ADMIN", "USUARIO")))
				.andExpect(redirectedUrl("/listarProdutos"));
	}

	@Test
	void usuarioLogadoAcessaPaginas() throws Exception {
		mvc.perform(get("/menu").with(user("joao").roles("USUARIO"))).andExpect(status().isOk());
		mvc.perform(get("/listarProdutos").with(user("joao").roles("USUARIO"))).andExpect(status().isOk());
		mvc.perform(get("/formCadastrarProduto").with(user("joao").roles("USUARIO"))).andExpect(status().isOk());
	}

}
