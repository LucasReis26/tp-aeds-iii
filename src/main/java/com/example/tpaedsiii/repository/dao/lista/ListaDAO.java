package com.example.tpaedsiii.repository.dao.lista;

import com.example.tpaedsiii.repository.bd.base.Arquivo;
import com.example.tpaedsiii.repository.bd.filme.Filme;
import com.example.tpaedsiii.repository.bd.indexes.base.HashExtensivel;
import com.example.tpaedsiii.repository.bd.indexes.ParListaFilme;
import com.example.tpaedsiii.repository.bd.indexes.ParUsuarioLista;
import com.example.tpaedsiii.repository.bd.lista.Lista;
import com.example.tpaedsiii.repository.dao.filme.FilmeDAO; // Dependência necessária

import java.util.ArrayList;
import java.util.List;


public class ListaDAO {

    private final Arquivo<Lista> arqListas;
    private final HashExtensivel<ParUsuarioLista> idxUsuarioLista;
    private final HashExtensivel<ParListaFilme> idxListaFilme;

    public ListaDAO() throws Exception {
        arqListas = new Arquivo<>("data/Listas.db", Lista.class.getConstructor());
        
        idxUsuarioLista = new HashExtensivel<>(ParUsuarioLista.class.getConstructor(), 10, "./data/idx_user_lista_d.db", "./data/idx_user_lista_c.db");
        
        idxListaFilme = new HashExtensivel<>(ParListaFilme.class.getConstructor(), 20, "./data/idx_lista_filme_d.db", "./data/idx_lista_filme_c.db");
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
        return arqListas.delete(listaId);
    }
    

    public Lista buscarListaCompleta(int listaId, FilmeDAO filmeDAO) throws Exception {
        Lista lista = arqListas.read(listaId);

        if (lista != null) {
            List<ParListaFilme> pares = idxListaFilme.readAll(listaId);
            ArrayList<Filme> filmes = new ArrayList<>();
            
            for (ParListaFilme par : pares) {
                Filme f = filmeDAO.buscarFilme(par.getFilmeId());
                if (f != null) {
                    filmes.add(f);
                }
            }
            lista.setFilmes(filmes);
        }
        return lista;
    }


    public List<Lista> buscarListasPorUsuario(int userId, FilmeDAO filmeDAO) throws Exception {
        List<ParUsuarioLista> pares = idxUsuarioLista.readAll(userId);
        List<Lista> listas = new ArrayList<>();

        for (ParUsuarioLista par : pares) {
            Lista l = buscarListaCompleta(par.getListaId(), filmeDAO);
            if (l != null) {
                listas.add(l);
            }
        }
        return listas;
    }
    

    public Lista buscarListaPorNomeEUsuario(String nome, int userId, FilmeDAO filmeDAO) throws Exception {
        List<Lista> listasDoUsuario = buscarListasPorUsuario(userId, filmeDAO);
        
        for (Lista lista : listasDoUsuario) {
            if (lista.getNome().equalsIgnoreCase(nome)) {
                return lista; 
            }
        }
        return null; 
    }


    public void criarListasPadraoParaUsuario(int userId, FilmeDAO filmeDAO) throws Exception {
        String[] nomesPadrao = {"Favoritos", "Assistidos", "Quero Assistir"};
        
        for (String nome : nomesPadrao) {
            if (buscarListaPorNomeEUsuario(nome, userId, filmeDAO) == null) {
                Lista listaPadrao = new Lista(0, userId, nome, true);
                incluirLista(listaPadrao);
            }
        }
    }
}