package com.example.tpaedsiii.controller;

import com.example.tpaedsiii.model.review.Review;
import com.example.tpaedsiii.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Review review) {
        try {
            int novoId = reviewService.criarReview(review);
            review.setId(novoId);
            return ResponseEntity.created(URI.create("/api/reviews/" + novoId)).body(review);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400 Bad Request se user/filme não existe
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReviewsByUser(@PathVariable int userId) {
        try {
            List<Review> reviews = reviewService.buscarPorUsuario(userId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/filme/{filmeId}")
    public ResponseEntity<?> getReviewsByFilme(@PathVariable int filmeId) {
        try {
            List<Review> reviews = reviewService.buscarPorFilme(filmeId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable int id) {
        try {
            return reviewService.deletarReview(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}