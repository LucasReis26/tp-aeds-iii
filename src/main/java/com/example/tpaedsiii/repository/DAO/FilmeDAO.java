package com.example.tpaedsiii.repository.DAO;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

import com.example.tpaedsiii.repository.BD.Arquivo;
import com.example.tpaedsiii.repository.BD.Filme;

public class FilmeDAO {
    private final Arquivo<Filme> arqFilmes;

    public FilmeDAO() throws Exception {
        arqFilmes = new Arquivo<>("filmes", Filme.class.getConstructor());
    }

    public int incluirFilme(Filme filme) throws Exception {
        return arqFilmes.create(filme);
    }

    public Filme buscarFilme(int id) throws Exception {
        return arqFilmes.read(id);
    }

    public boolean alterarFilme(Filme filme) throws Exception {
        return arqFilmes.update(filme);
    }

    public boolean excluirFilme(int id) throws Exception {
        return arqFilmes.delete(id);
    }

}

