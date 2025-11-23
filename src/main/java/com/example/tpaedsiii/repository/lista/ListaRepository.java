package com.example.tpaedsiii.repository.lista;

import com.example.tpaedsiii.model.filme.Filme;
import com.example.tpaedsiii.model.lista.Lista;
import com.example.tpaedsiii.repository.bd.base.Arquivo;
import com.example.tpaedsiii.repository.bd.indexes.ParesHash.ParListaFilme;
import com.example.tpaedsiii.repository.bd.indexes.ParesHash.ParUsuarioLista;
import com.example.tpaedsiii.repository.bd.indexes.base.HashExtensivel;
import com.example.tpaedsiii.repository.filme.FilmeRepository;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class ListaRepository {

    private Arquivo<Lista> arqListas;
    private HashExtensivel<ParUsuarioLista> idxUsuarioLista;
    private HashExtensivel<ParListaFilme> idxListaFilme;

    public ListaRepository() {}

    @PostConstruct
    public void init() throws Exception {
        arqListas = new Arquivo<>("data/Listas.db", Lista.class.getConstructor());
        idxUsuarioLista = new HashExtensivel<>(ParUsuarioLista.class.getConstructor(), 10, "data/idx_user_lista_d.db", "data/idx_user_lista_c.db");
        idxListaFilme = new HashExtensivel<>(ParListaFilme.class.getConstructor(), 20, "data/idx_lista_filme_d.db", "data/idx_lista_filme_c.db");
    }

    public int incluirLista(Lista lista) throws Exception {
        lista.setFilmes(new ArrayList<>());
        int newId = arqListas.create(lista);
        if (newId != -1) {
            idxUsuarioLista.create(new ParUsuarioLista(lista.getUserId(), newId));
        }
        return newId;
    }

    public void adicionarFilmeEmLista(int listaId, int filmeId) throws Exception {
        idxListaFilme.create(new ParListaFilme(listaId, filmeId));
    }

    public boolean alterarLista(Lista lista) throws Exception {
        lista.setFilmes(new ArrayList<>());
        return arqListas.update(lista);
    }

    public boolean excluirLista(int listaId) throws Exception {
        // Lógica de exclusão completa também precisaria de limpar os índices
        return arqListas.delete(listaId);
    }

    public Lista buscarListaMetadata(int listaId) throws Exception {
        return arqListas.read(listaId);
    }
    
    public List<Lista> buscarTodasListasMetadata() throws Exception {
        return arqListas.readAll();
    }
    
    public List<Integer> buscarFilmeIdsPorLista(int listaId) throws Exception {
        List<ParListaFilme> pares = idxListaFilme.readAll(listaId);
        List<Integer> filmeIds = new ArrayList<>();
        for (ParListaFilme par : pares) {
            filmeIds.add(par.getFilmeId());
        }
        return filmeIds;
    }

    public List<Lista> buscarMetadadosDeListasPorUsuario(int userId) throws Exception {
        // 1. Consulta o índice para obter os ponteiros (pares).
        List<ParUsuarioLista> pares = idxUsuarioLista.readAll(userId);
        List<Lista> listas = new ArrayList<>();

        // 2. Para cada ponteiro, busca o metadado da lista correspondente.
        for (ParUsuarioLista par : pares) {
            Lista l = this.buscarListaMetadata(par.getListaId());
            if (l != null) {
                listas.add(l);
            }
        }
        return listas;
    }

    public boolean removerFilmeDaLista(int listaId, int filmeId) throws Exception {
        ParListaFilme parParaDeletar = new ParListaFilme(listaId, filmeId);
        return idxListaFilme.delete(parParaDeletar);
    }
}

