package com.example.tpaedsiii.service;

import com.example.tpaedsiii.model.filme.Filme;
import com.example.tpaedsiii.repository.filme.FilmeRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class FilmeService {

    private final FilmeRepository filmeRepository;

    public FilmeService(FilmeRepository filmeRepository) {
        this.filmeRepository = filmeRepository;
    }

    public List<Filme> listarTodos() throws Exception {
        List<Filme> filmes = filmeRepository.readAll();
        if (filmes.isEmpty()) {
            throw new Exception("Nenhum filme cadastrado no sistema");
        }
        return filmes;
    }

    public Filme buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID do filme deve ser maior que zero");
        }
        
        Filme filme = filmeRepository.buscarFilme(id);
        if (filme == null) {
            throw new Exception("Filme com ID " + id + " não encontrado");
        }
        return filme;
    }

    public int criarFilme(Filme filme) throws Exception {
        validarFilme(filme);
        int novoId = filmeRepository.incluirFilme(filme);
        if (novoId <= 0) {
            throw new Exception("Falha ao criar filme - ID inválido retornado");
        }
        return novoId;
    }

    public List<Filme> buscarPorTitulo(String titulo) throws Exception {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new Exception("Título não pode ser vazio");
        }
        
        if (titulo.length() < 2) {
            throw new Exception("Título deve ter pelo menos 2 caracteres");
        }
        
        List<Filme> filmes = filmeRepository.buscarPorTitulo(titulo.trim());
        if (filmes.isEmpty()) {
            throw new Exception("Nenhum filme encontrado com o título: " + titulo);
        }
        return filmes;
    }
    
    public boolean deletarFilme(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("ID do filme deve ser maior que zero");
        }
        
        // Verificar se o filme existe antes de deletar
        Filme filmeExistente = filmeRepository.buscarFilme(id);
        if (filmeExistente == null) {
            throw new Exception("Filme com ID " + id + " não encontrado para exclusão");
        }
        
        boolean deletado = filmeRepository.excluirFilme(id);
        if (!deletado) {
            throw new Exception("Falha ao excluir filme com ID " + id);
        }
        
        return true;
    }

    public boolean atualizarFilme(Filme filme) throws Exception {
        if (filme.getId() <= 0) {
            throw new Exception("ID do filme deve ser maior que zero para atualização");
        }
        
        validarFilme(filme);
        
        // Verificar se o filme existe antes de atualizar
        Filme filmeExistente = filmeRepository.buscarFilme(filme.getId());
        if (filmeExistente == null) {
            throw new Exception("Filme com ID " + filme.getId() + " não encontrado para atualização");
        }
        
        boolean atualizado = filmeRepository.alterarFilme(filme);
        if (!atualizado) {
            throw new Exception("Falha ao atualizar filme com ID " + filme.getId());
        }
        
        return true;
    }

    // Método de validação mantido, mas lança Exception em vez de FilmeException
    private void validarFilme(Filme filme) throws Exception {
        if (filme == null) {
            throw new Exception("Filme não pode ser nulo");
        }
        
        // Validação do título
        if (filme.getTitle() == null || filme.getTitle().trim().isEmpty()) {
            throw new Exception("Título do filme é obrigatório");
        }
        if (filme.getTitle().length() < 2) {
            throw new Exception("Título do filme deve ter pelo menos 2 caracteres");
        }
        if (filme.getTitle().length() > 500) {
            throw new Exception("Título do filme não pode exceder 500 caracteres");
        }
        
        // Validação do score
        if (filme.getScore() < 0 || filme.getScore() > 100) {
            throw new Exception("Score deve estar entre 0 e 100");
        }
        
        // Validação da data de lançamento
        if (filme.getReleaseDate() == null) {
            throw new Exception("Data de lançamento é obrigatória");
        }
        
        LocalDate dataMinima = LocalDate.of(1888, 1, 1);
        LocalDate dataMaxima = LocalDate.now().plusYears(5);
        
        if (filme.getReleaseDate().isBefore(dataMinima)) {
            throw new Exception("Data de lançamento não pode ser anterior a " + dataMinima.getYear());
        }
        
        if (filme.getReleaseDate().isAfter(dataMaxima)) {
            throw new Exception("Data de lançamento não pode ser mais de 5 anos no futuro");
        }
        
        // Validação da duração
        if (filme.getDuration() <= 0) {
            throw new Exception("Duração do filme deve ser maior que zero");
        }
        
        if (filme.getDuration() > 1000 * 60) {
            throw new Exception("Duração do filme excede o limite máximo");
        }
        
        // Validação dos diretores
        if (filme.getDirectors() == null || filme.getDirectors().isEmpty()) {
            throw new Exception("Pelo menos um diretor deve ser informado");
        }
        
        for (String diretor : filme.getDirectors()) {
            if (diretor == null || diretor.trim().isEmpty()) {
                throw new Exception("Nome do diretor não pode ser vazio");
            }
            if (diretor.length() < 2) {
                throw new Exception("Nome do diretor deve ter pelo menos 2 caracteres");
            }
            if (diretor.length() > 100) {
                throw new Exception("Nome do diretor não pode exceder 100 caracteres");
            }
        }
        
        // Validação dos atores
        if (filme.getActors() != null) {
            for (String ator : filme.getActors()) {
                if (ator != null && !ator.trim().isEmpty()) {
                    if (ator.length() < 2) {
                        throw new Exception("Nome do ator deve ter pelo menos 2 caracteres");
                    }
                    if (ator.length() > 100) {
                        throw new Exception("Nome do ator não pode exceder 100 caracteres");
                    }
                }
            }
        }
        
        // Validação do rating
        if (filme.getRating() < 0 || filme.getRating() > 100) {
            throw new Exception("Rating deve estar entre 0 e 100");
        }
    }
}