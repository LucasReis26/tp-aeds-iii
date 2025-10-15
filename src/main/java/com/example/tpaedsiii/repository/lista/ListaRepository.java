package com.example.tpaedsiii.repository.lista;

import com.example.tpaedsiii.model.filme.Filme;
import com.example.tpaedsiii.model.lista.Lista;
import com.example.tpaedsiii.repository.bd.indexes.base.ArvoreBMais;
import com.example.tpaedsiii.repository.bd.indexes.ParesArvoreB.ParIntInt;
import com.example.tpaedsiii.repository.bd.indexes.ParesHash.ParUsuarioLista;
import com.example.tpaedsiii.repository.bd.indexes.base.HashExtensivel;
import com.example.tpaedsiii.repository.filme.FilmeRepository;

import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ListaRepository {

    private ArvoreBMais<Lista> arvoreListas;
    private HashExtensivel<ParUsuarioLista> idxUsuarioLista;
    private ArvoreBMais<ParIntInt> idxListaFilme;
    private static final String ID_COUNTER_FILE = "data/lista_id_counter.db";
    
    private final FilmeRepository filmeRepository;

    public ListaRepository(FilmeRepository filmeRepository) {
        this.filmeRepository = filmeRepository;
    }

    @PostConstruct
    public void init() throws Exception {
        new File("data").mkdirs();
        
        arvoreListas = new ArvoreBMais<Lista>(Lista.class.getConstructor(), 5, "data/Listas_bplus.db");
        idxUsuarioLista = new HashExtensivel<>(ParUsuarioLista.class.getConstructor(), 10, "data/idx_user_lista_d.db", "data/idx_user_lista_c.db");
        idxListaFilme = new ArvoreBMais<ParIntInt>(ParIntInt.class.getConstructor(), 20, "data/idx_lista_filme_bplus.db");
    }

    private synchronized int getNextId() throws Exception {
        int nextId = 1;
        File counterFile = new File(ID_COUNTER_FILE);
        if (counterFile.exists() && counterFile.length() > 0) {
            try (DataInputStream dis = new DataInputStream(new FileInputStream(counterFile))) {
                nextId = dis.readInt() + 1;
            }
        }
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(counterFile))) {
            dos.writeInt(nextId);
        }
        return nextId;
    }

    public int incluirLista(Lista lista) throws Exception {
        int newId = getNextId();
        lista.setId(newId);
        
        // 1. Insere a lista no armazenamento primário (árvore de listas por ID).
        arvoreListas.create(lista);
        
        // 2. Cria a entrada no índice que liga o usuário a esta nova lista.
        idxUsuarioLista.create(new ParUsuarioLista(lista.getUserId(), newId));
        
        return newId;
    }

    public void adicionarFilmeEmLista(int listaId, int filmeId) throws Exception {
        // Insere a relação no índice N-M. A chave é o listaId, o valor é o filmeId.
        idxListaFilme.create(new ParIntInt(listaId, filmeId));
    }

    public boolean removerFilmeDaLista(int listaId, int filmeId) throws Exception {
        return idxListaFilme.delete(new ParIntInt(listaId, filmeId));
    }

    public boolean alterarLista(Lista lista) throws Exception {
 
        return arvoreListas.update(lista);
    }

    public boolean excluirLista(int listaId) throws Exception {
        Lista listaParaExcluir = buscarLista(listaId);
        if (listaParaExcluir == null) return false;
        return arvoreListas.delete(listaParaExcluir);
    }


    private Lista buscarLista(int listaId) throws Exception {
        ArrayList<Lista> resultadoBusca = arvoreListas.read(new Lista(listaId, 0, "", false));
        return resultadoBusca.isEmpty() ? null : resultadoBusca.get(0);
    }

    public Lista buscarListaCompleta(int listaId) throws Exception {
        Lista lista = buscarLista(listaId);
        if (lista != null) {
            ArrayList<ParIntInt> pares = idxListaFilme.read(new ParIntInt(listaId, 0));
            ArrayList<Filme> filmes = new ArrayList<>();
            for (ParIntInt par : pares) {
                if (par.getChave() == listaId) {
                    Filme f = null;
                    try {
                        f = filmeRepository.buscarFilme(par.getValor());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (f != null) {
                        filmes.add(f);
                    }
                }
            }
            lista.setFilmes(filmes);
        }
        return lista;
    }
    
    public List<Lista> buscarListasPorUsuario(int userId) throws Exception {
        List<ParUsuarioLista> pares = idxUsuarioLista.readAll(userId);
        List<Lista> listas = new ArrayList<>();
        for (ParUsuarioLista par : pares) {
            Lista l = buscarListaCompleta(par.getListaId());
            if (l != null) {
                listas.add(l);
            }
        }
        return listas;
    }
    
    public Lista buscarListaPorNomeEUsuario(String nome, int userId) throws Exception {
        List<Lista> listasDoUsuario = buscarListasPorUsuario(userId);
        for (Lista lista : listasDoUsuario) {
            if (lista.getNome().equalsIgnoreCase(nome)) {
                return lista;
            }
        }
        return null;
    }

    public void criarListasPadraoParaUsuario(int userId) throws Exception {
        String[] nomesPadrao = { "Favoritos", "Assistidos", "Quero Assistir" };
        for (String nome : nomesPadrao) {
            if (buscarListaPorNomeEUsuario(nome, userId) == null) {
                Lista listaPadrao = new Lista(0, userId, nome, true);
                incluirLista(listaPadrao);
            }
        }
    }
}

