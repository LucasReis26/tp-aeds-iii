package com.example.tpaedsiii.service;

import com.example.tpaedsiii.model.filme.Filme;
import com.example.tpaedsiii.repository.filme.FilmeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FilmeService {

    private final FilmeRepository filmeRepository;

    public FilmeService(FilmeRepository filmeRepository) {
        this.filmeRepository = filmeRepository;
    }

    public Filme buscarPorId(int id) throws Exception {
        return filmeRepository.buscarFilme(id);
    }

    public int criarFilme(Filme filme) throws Exception {
        return filmeRepository.incluirFilme(filme);
    }

    public List<Filme> buscarPorTitulo(String titulo) throws Exception {
        return filmeRepository.buscarPorTitulo(titulo);
    }
    
    public boolean deletarFilme(int id) throws Exception {
        // Lógica de Negócio Futura: Antes de deletar um filme, remover de todas as listas e deletar suas reviews.
        return filmeRepository.excluirFilme(id);
    }
}