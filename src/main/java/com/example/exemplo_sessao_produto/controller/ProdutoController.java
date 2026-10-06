package com.example.exemplo_sessao_produto.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.exemplo_sessao_produto.model.Produto;
import com.example.exemplo_sessao_produto.service.ProdutoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProdutoController {

    @Autowired
    ProdutoService produtoService;

    @GetMapping({ "/", "/menu" })
    public String menu(HttpSession sessao, Model model) {
        // Informações da sessão HTTP do usuário logado (exibidas no menu)
        model.addAttribute("sessaoId", sessao.getId());
        model.addAttribute("sessaoCriada", new java.util.Date(sessao.getCreationTime()));
        model.addAttribute("sessaoTimeout", sessao.getMaxInactiveInterval() / 60);
        return "menu";
    }

    @GetMapping("/formCadastrarProduto")
    public String formCadastrarProduto() {
        return "formCadastrarProduto";
    }

    @PostMapping("/cadastrarProduto")
    public String cadastrarProduto(@RequestParam String nome, @RequestParam(required = false) String descricao,
            @RequestParam(required = false) MultipartFile logo, Model model) {
        if (nome.isBlank()) {
            model.addAttribute("erroProduto", "Informe o nome do produto!");
            return "formCadastrarProduto";
        }
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setDescricao(descricao);
        try {
            produtoService.salvar(produto, logo);
            model.addAttribute("mensagemSucesso", "Produto cadastrado com sucesso!");
        } catch (IOException e) {
            model.addAttribute("erroProduto", "Erro ao salvar o logo do produto!");
        }
        return "formCadastrarProduto";
    }

    @GetMapping("/listarProdutos")
    public String listarProdutos(Model model) {
        model.addAttribute("produtos", produtoService.listar());
        return "listarProdutos";
    }

    // Protegido na configuração de segurança: somente ADMIN
    @PostMapping("/excluirProduto/{id}")
    public String excluirProduto(@PathVariable Long id) throws IOException {
        produtoService.excluir(id);
        return "redirect:/listarProdutos";
    }
}
