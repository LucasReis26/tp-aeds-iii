package com.example.tpaedsiii.repository.filme;

import com.example.tpaedsiii.model.filme.Filme;
import com.example.tpaedsiii.repository.bd.indexes.base.HashExtensivel;
import com.example.tpaedsiii.repository.bd.indexes.base.ArvoreBMais;
import com.example.tpaedsiii.repository.bd.indexes.ParesArvoreB.ParIntInt;
import com.example.tpaedsiii.repository.bd.indexes.ParesArvoreB.ParStringInt;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

@Repository
public class FilmeRepository {

    private HashExtensivel<Filme> hashFilmes;
    
    private ArvoreBMais<ParStringInt> idxTituloFilme;

    private ArvoreBMais<ParIntInt> idxScoreFilme;

    public FilmeRepository() {
    }
    
    @PostConstruct
    public void init() throws Exception {
        new File("data").mkdirs();

        hashFilmes = new HashExtensivel<>(
            Filme.class.getConstructor(), 
            4, 
            "data/filmes_d.db", 
            "data/filmes_c.db"
        );
        
        idxTituloFilme = new ArvoreBMais<>(
            ParStringInt.class.getConstructor(), 
            5, 
            "data/idx_titulo_filme.db"
        );

        idxScoreFilme = new ArvoreBMais<>(
            ParIntInt.class.getConstructor(),
            5,
            "data/idx_score_filme.db"
        );
    }


    public int incluirFilme(Filme filme) throws Exception {
        int novoId = hashFilmes.create(filme);
        filme.setId(novoId); 
        idxTituloFilme.create(new ParStringInt(filme.getTitle().toLowerCase(), novoId));
        
        idxScoreFilme.create(new ParIntInt(filme.getScore(), novoId));
        
        return novoId;
    }

    
    public Filme buscarFilme(int id) throws Exception {
        return hashFilmes.read(id);
    }

   
    public boolean alterarFilme(Filme filme) throws Exception {
        Filme filmeAntigo = buscarFilme(filme.getId());
        if (filmeAntigo == null) {
            throw new Exception("Filme com ID " + filme.getId() + " não encontrado para alteração.");
        }

        idxTituloFilme.delete(new ParStringInt(filmeAntigo.getTitle().toLowerCase(), filme.getId()));
        idxScoreFilme.delete(new ParIntInt(filmeAntigo.getScore(), filme.getId()));

        boolean sucesso = hashFilmes.update(filme);

        if (sucesso) {
            idxTituloFilme.create(new ParStringInt(filme.getTitle().toLowerCase(), filme.getId()));
            idxScoreFilme.create(new ParIntInt(filme.getScore(), filme.getId()));
        }
        return sucesso;
    }

  
    public boolean excluirFilme(int id) throws Exception {
        Filme filmeParaExcluir = buscarFilme(id);
        if (filmeParaExcluir == null) {
            return false;
        }
        
        idxTituloFilme.delete(new ParStringInt(filmeParaExcluir.getTitle().toLowerCase(), id));
        idxScoreFilme.delete(new ParIntInt(filmeParaExcluir.getScore(), id));
        
        return hashFilmes.delete(id);
    }

  
    public List<Filme> buscarPorTitulo(String termoBusca) throws Exception {
        ArrayList<ParStringInt> pares = idxTituloFilme.read(new ParStringInt(termoBusca.toLowerCase(), 0));
        
        List<Filme> resultados = new ArrayList<>();
        for (ParStringInt par : pares) {
            if (par.getChave().equals(termoBusca.toLowerCase())) {
                 Filme filme = buscarFilme(par.getValor());
                 if (filme != null) {
                    resultados.add(filme);
                 }
            }
        }
        return resultados;
    }

    public List<Filme> buscarPorFaixaDeScore(int notaMin, int notaMax) throws Exception {
        List<ParIntInt> pares = idxScoreFilme.readRange(new ParIntInt(notaMin, 0), new ParIntInt(notaMax, Integer.MAX_VALUE));
        
        List<Filme> resultados = new ArrayList<>();
        for (ParIntInt par : pares) {
            Filme filme = buscarFilme(par.getValor());
            if (filme != null) {
                resultados.add(filme);
            }
        }
        
        return resultados;
    }
}

