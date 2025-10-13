package com.example.tpaedsiii.repository.dao.filme;

import com.example.tpaedsiii.repository.bd.filme.Filme;
import com.example.tpaedsiii.repository.bd.indexes.base.HashExtensivel;

public class FilmeDAO {
    private final HashExtensivel<Filme> hashFilmes;

    public FilmeDAO() throws Exception {
        hashFilmes = new HashExtensivel<>(Filme.class.getConstructor(), 4, "data/filmes_d.db", "./data/filmes_c.db");
    }

    public int incluirFilme(Filme filme) throws Exception {
        if (hashFilmes.read(filme.getId()) != null) {
            throw new Exception("Filme com ID " + filme.getId() + " já existe.");
        }
        hashFilmes.create(filme);
        return filme.getId();
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
}