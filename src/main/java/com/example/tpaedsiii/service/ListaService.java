package com.example.tpaedsiii.service;

import com.example.tpaedsiii.model.lista.Lista;
import com.example.tpaedsiii.repository.filme.FilmeRepository;
import com.example.tpaedsiii.repository.lista.ListaRepository;
import org.springframework.stereotype.Service;
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

    public void criarListasPadrao(int userId) throws Exception {
        listaRepository.criarListasPadraoParaUsuario(userId, filmeRepository);
    }

    public void adicionarFilme(int listaId, int filmeId) throws Exception {
        // Regra de Negócio: Verificar se a lista e o filme existem.
        if (listaRepository.buscarListaCompleta(listaId, filmeRepository) == null) {
            throw new Exception("Lista com ID " + listaId + " não existe.");
        }
        if (filmeRepository.buscarFilme(filmeId) == null) {
            throw new Exception("Filme com ID " + filmeId + " não existe.");
        }
        listaRepository.adicionarFilmeEmLista(listaId, filmeId);
    }

    public Lista atualizarLista(int listaId, String novoNome) throws Exception {
        Lista lista = listaRepository.buscarListaCompleta(listaId, filmeRepository);
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

    public List<Lista> buscarPorUsuario(int userId) throws Exception {
        return listaRepository.buscarListasPorUsuario(userId, filmeRepository);
    }
    public List<Lista> buscarTodas() throws Exception {
        return listaRepository.buscarTodasListas(filmeRepository);
    }

    public boolean deletarLista(int listaId) throws Exception {
        // Lógica Futura: Remover todas as relações ParListaFilme antes de deletar a lista.
        return listaRepository.excluirLista(listaId);
    }
}