package com.example.tpaedsiii.service;

import com.example.tpaedsiii.model.filme.Filme;
import com.example.tpaedsiii.model.lista.Lista;
import com.example.tpaedsiii.repository.filme.FilmeRepository;
import com.example.tpaedsiii.repository.lista.ListaRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ListaService {

    private final ListaRepository listaRepository;
    private final FilmeRepository filmeRepository; 

    public ListaService(ListaRepository listaRepository, FilmeRepository filmeRepository) {
        this.listaRepository = listaRepository;
        this.filmeRepository = filmeRepository;
    }

   
    public int criarListaPersonalizada(Lista lista) throws Exception {
        return listaRepository.incluirLista(lista);
    }

    
    public void criarListasPadraoParaUsuario(int userId) throws Exception {
        List<Lista> listasDoUsuario = this.buscarPorUsuario(userId);
        String[] nomesPadrao = { "Favoritos", "Assistidos", "Quero Assistir" };

        for (String nome : nomesPadrao) {
            boolean jaExiste = listasDoUsuario.stream().anyMatch(l -> l.getNome().equalsIgnoreCase(nome));
            if (!jaExiste) {
                Lista listaPadrao = new Lista(0, userId, nome, true);
                listaRepository.incluirLista(listaPadrao);
            }
        }
    }

 
    public void adicionarFilme(int listaId, int filmeId) throws Exception {
        // Regra de negócio: Verificar se a lista e o filme existem antes de criar a relação.
        if (listaRepository.buscarListaMetadata(listaId) == null) {
            throw new Exception("Lista com ID " + listaId + " não existe.");
        }
        if (filmeRepository.buscarFilme(filmeId) == null) {
            throw new Exception("Filme com ID " + filmeId + " não existe.");
        }
        listaRepository.adicionarFilmeEmLista(listaId, filmeId);
    }

   
    public Lista atualizarLista(int listaId, String novoNome) throws Exception {
        Lista lista = listaRepository.buscarListaMetadata(listaId);
        if (lista == null) {
            throw new Exception("Lista com ID " + listaId + " não encontrada.");
        }
        lista.setNome(novoNome);
        if (!listaRepository.alterarLista(lista)) {
            throw new Exception("Falha ao atualizar a lista.");
        }
        return lista;
    }

    public boolean removerFilme(int listaId, int filmeId) throws Exception {
        return listaRepository.removerFilmeDaLista(listaId, filmeId);
    }
    

    public Lista buscarListaCompleta(int listaId) throws Exception {
        Lista lista = listaRepository.buscarListaMetadata(listaId);
        if (lista == null) return null;

        List<Integer> filmeIds = listaRepository.buscarFilmeIdsPorLista(listaId);
        ArrayList<Filme> filmes = new ArrayList<>();

        for (Integer filmeId : filmeIds) {
            Filme f = filmeRepository.buscarFilme(filmeId);
            if (f != null) {
                filmes.add(f);
            }
        }
        lista.setFilmes(filmes);
        return lista;
    }

  
    public List<Lista> buscarPorUsuario(int userId) throws Exception {
        List<Lista> listasMetadata = listaRepository.buscarMetadadosDeListasPorUsuario(userId);
        
        for (Lista lista : listasMetadata) {
            List<Integer> filmeIds = listaRepository.buscarFilmeIdsPorLista(lista.getId());
            ArrayList<Filme> filmes = new ArrayList<>();
            for (Integer filmeId : filmeIds) {
                Filme f = filmeRepository.buscarFilme(filmeId);
                if (f != null) {
                    filmes.add(f);
                }
            }
            lista.setFilmes(filmes);
        }
        return listasMetadata;
    }
    
 
    public List<Lista> buscarTodas() throws Exception {
        List<Lista> listas = listaRepository.buscarTodasListasMetadata();
        for (Lista lista : listas) {
            List<Integer> filmeIds = listaRepository.buscarFilmeIdsPorLista(lista.getId());
            ArrayList<Filme> filmes = new ArrayList<>();
            for (Integer filmeId : filmeIds) {
                Filme f = filmeRepository.buscarFilme(filmeId);
                if (f != null) {
                    filmes.add(f);
                }
            }
            lista.setFilmes(filmes);
        }
        return listas;
    }

    public boolean deletarLista(int listaId) throws Exception {
        return listaRepository.excluirLista(listaId);
    }
}

