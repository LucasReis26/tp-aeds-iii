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