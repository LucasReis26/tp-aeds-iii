package com.example.tpaedsiii.repository.filme;

import com.example.tpaedsiii.model.filme.Filme;
import com.example.tpaedsiii.repository.bd.indexes.base.HashExtensivel;

import jakarta.annotation.PostConstruct;

import java.util.List; // Adicionado para readAll

import org.springframework.stereotype.Repository;

import java.util.ArrayList; // Adicionado para buscarPorTitulo

@Repository
public class FilmeRepository {
    private HashExtensivel<Filme> hashFilmes;

    public FilmeRepository() throws Exception {
      
    }
    
    @PostConstruct
    public void init() throws Exception{
      hashFilmes = new HashExtensivel<>(
            Filme.class.getConstructor(), 
            4, 
            "data/filmes_d.db", 
            "data/filmes_c.db"
        );  
    }


    public int incluirFilme(Filme filme) throws Exception {
        return hashFilmes.create(filme);
    }

    public Filme buscarFilme(int id) throws Exception {
        return hashFilmes.read(id);
    }

    public boolean alterarFilme(Filme filme) throws Exception {
        return hashFilmes.update(filme);
    }

    public boolean excluirFilme(int id) throws Exception {
        return hashFilmes.delete(id);
    }

    public List<Filme> readAll() throws Exception {
        return hashFilmes.readAll();
    }

    public List<Filme> buscarPorTitulo(String termoBusca) throws Exception {
        List<Filme> todosOsFilmes = this.readAll();
        List<Filme> resultados = new ArrayList<>();
        String termoBuscaLower = termoBusca.toLowerCase();

        for (Filme filme : todosOsFilmes) {
            if (filme.getTitle().toLowerCase().contains(termoBuscaLower)) {
                resultados.add(filme);
            }
        }
        return resultados;
    }
}