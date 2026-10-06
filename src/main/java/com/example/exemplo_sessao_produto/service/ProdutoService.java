package com.example.exemplo_sessao_produto.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.exemplo_sessao_produto.model.Produto;
import com.example.exemplo_sessao_produto.repository.ProdutoDAO;

@Service
public class ProdutoService {

    @Autowired
    ProdutoDAO produtoDao;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public List<Produto> listar() {
        return produtoDao.findAll();
    }

    public void salvar(Produto produto, MultipartFile logo) throws IOException {
        if (logo != null && !logo.isEmpty()) {
            produto.setLogo(salvarArquivo(logo));
        }
        produtoDao.save(produto);
    }

    public void excluir(Long id) throws IOException {
        Produto produto = produtoDao.findById(id).orElse(null);
        if (produto == null)
            return;
        if (produto.getLogo() != null)
            Files.deleteIfExists(Paths.get(uploadDir).resolve(produto.getLogo()));
        produtoDao.delete(produto);
    }

    // Salva o arquivo com um nome único para evitar sobrescrita e nomes maliciosos
    private String salvarArquivo(MultipartFile arquivo) throws IOException {
        Path pasta = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(pasta);

        String extensao = StringUtils.getFilenameExtension(arquivo.getOriginalFilename());
        String nomeArquivo = UUID.randomUUID() + (extensao != null ? "." + extensao.toLowerCase() : "");

        try (InputStream entrada = arquivo.getInputStream()) {
            Files.copy(entrada, pasta.resolve(nomeArquivo), StandardCopyOption.REPLACE_EXISTING);
        }
        return nomeArquivo;
    }
}
