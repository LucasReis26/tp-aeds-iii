package com.example.tpaedsiii.repository.review;

import com.example.tpaedsiii.repository.bd.indexes.base.HashExtensivel;

import jakarta.annotation.PostConstruct;

import com.example.tpaedsiii.repository.bd.indexes.ParUsuarioReview;
import com.example.tpaedsiii.model.review.Review;
import com.example.tpaedsiii.repository.bd.indexes.ParFilmeReview;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class ReviewRepository {
    private  HashExtensivel<Review> hashReviews;
    private  HashExtensivel<ParUsuarioReview> idxUsuarioReview;
    private  HashExtensivel<ParFilmeReview> idxFilmeReview;

    public ReviewRepository(){}

    @PostConstruct
    public void init() throws Exception{
        hashReviews = new HashExtensivel<>(Review.class.getConstructor(), 4, "data/reviews_d.db", "./data/reviews_c.db");
        idxUsuarioReview = new HashExtensivel<>(ParUsuarioReview.class.getConstructor(), 10, "./data/idx_user_rev_d.db", "./data/idx_user_rev_c.db");
        idxFilmeReview = new HashExtensivel<>(ParFilmeReview.class.getConstructor(), 10, "./data/idx_film_rev_d.db", "./data/idx_film_rev_c.db");
    }
    

     public int create(Review review) throws Exception {
        int novoId = hashReviews.create(review);
        
        idxUsuarioReview.create(new ParUsuarioReview(review.getUserId(), novoId));
        idxFilmeReview.create(new ParFilmeReview(review.getFilmeId(), novoId));
        
        return novoId;
    }

    public Review read(int id) throws Exception {
        return hashReviews.read(id);
    }

    public boolean update(Review review) throws Exception {
        return hashReviews.update(review);
    }

    public boolean delete(int id) throws Exception {
        return hashReviews.delete(id);
    }

    public List<Review> buscarAvaliacoesPorUsuario(int userId) throws Exception {
        List<ParUsuarioReview> pares = idxUsuarioReview.readAll(userId);
        List<Review> reviews = new ArrayList<>();
        for (ParUsuarioReview par : pares) {
            Review r = hashReviews.read(par.getReviewId());
            if (r != null) {
                reviews.add(r);
            }
        }
        return reviews;
    }

    public List<Review> buscarAvaliacoesPorFilme(int filmeId) throws Exception {
        List<ParFilmeReview> pares = idxFilmeReview.readAll(filmeId);
        List<Review> reviews = new ArrayList<>();
        for (ParFilmeReview par : pares) {
            Review r = hashReviews.read(par.getReviewId());
            if (r != null) {
                reviews.add(r);
            }
        }
        return reviews;
    }

    public Review buscarReviewUnica(int userId, int filmeId) throws Exception {
        List<Review> reviewsDoFilme = buscarAvaliacoesPorFilme(filmeId);
        for (Review r : reviewsDoFilme) {
            if (r.getUserId() == userId) {
                return r;
            }
        }
        return null;
    }
}
