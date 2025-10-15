package com.example.tpaedsiii.service;

import com.example.tpaedsiii.model.review.Review;
import com.example.tpaedsiii.repository.filme.FilmeRepository;
import com.example.tpaedsiii.repository.review.ReviewRepository;
import com.example.tpaedsiii.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FilmeRepository filmeRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, FilmeRepository filmeRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.filmeRepository = filmeRepository;
    }

    public int criarReview(Review review) throws Exception {
        if (userRepository.buscarUser(review.getUserId()) == null) {
            throw new Exception("Usuário com ID " + review.getUserId() + " não existe.");
        }
        if (filmeRepository.buscarFilme(review.getFilmeId()) == null) {
            throw new Exception("Filme com ID " + review.getFilmeId() + " não existe.");
        }
        return reviewRepository.create(review);
    }
    public boolean atualizarReview(Review review) throws Exception {
        Review existente = reviewRepository.read(review.getId());
        if (existente == null) {
            throw new Exception("Review não encontrada");
        }
        // Apenas o usuário dono da review pode atualizar
        if (existente.getUserId() != review.getUserId()) {
            throw new Exception("Usuário não pode atualizar review de outro usuário");
        }
        // Atualiza nota e comentário (ou outros campos, se desejar)
        existente.setNota(review.getNota());
        existente.setComentario(review.getComentario());
        return reviewRepository.update(existente);
    }

    public List<Review> listarTodasReviews() throws Exception {
        return reviewRepository.listarTodasReviews();
    }

    public List<Review> buscarPorUsuario(int userId) throws Exception {
        return reviewRepository.buscarAvaliacoesPorUsuario(userId);
    }

    public List<Review> buscarPorFilme(int filmeId) throws Exception {
        return reviewRepository.buscarAvaliacoesPorFilme(filmeId);
    }
    
    public boolean deletarReview(int id) throws Exception {
        return reviewRepository.delete(id);
    }

    
}