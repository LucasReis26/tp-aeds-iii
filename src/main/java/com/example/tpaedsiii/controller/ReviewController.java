package com.example.tpaedsiii.controller;

import com.example.tpaedsiii.model.review.Review;
import com.example.tpaedsiii.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "Endpoints para criar e consultar avaliações (reviews)")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "Cria uma nova review de um usuário para um filme")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Review criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida (ex: usuário ou filme não existe)")
    })
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody Review review) {
        try {
            int novoId = reviewService.criarReview(review);
            review.setId(novoId);
            return ResponseEntity.created(URI.create("/api/reviews/" + novoId)).body(review);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Atualiza uma review existente. Requer o ID do usuário para verificação de permissão.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Review atualizada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Permissão negada (usuário não é o dono da review)"),
        @ApiResponse(responseCode = "404", description = "Review não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable int id, @RequestBody Review reviewData, @RequestHeader(value = "X-User-ID", required = true) int userId) {
        try {
            // O userId viria de um cabeçalho de autenticação num sistema real.
            boolean success = reviewService.atualizarReview(id, userId, reviewData.getNota(), reviewData.getComentario());
            return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
             if(e.getMessage().contains("Permissão negada")) {
                return ResponseEntity.status(403).body(e.getMessage());
             }
             return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Lista todas as reviews cadastradas no sistema")
    @GetMapping
    public ResponseEntity<?> getAllReviews() {
        try {
            List<Review> reviews = reviewService.listarTodasReviews();
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    @Operation(summary = "Busca uma review pelo seu ID único")
    @GetMapping("/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable int id) {
        // Implementação necessária no ReviewService e ReviewRepository
        // try { ... }
        return ResponseEntity.status(501).body("Ainda não implementado");
    }

    @Operation(summary = "Busca todas as reviews feitas por um usuário específico")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReviewsByUser(@PathVariable int userId) {
        try {
            List<Review> reviews = reviewService.buscarPorUsuario(userId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Busca todas as reviews de um filme específico")
    @GetMapping("/filme/{filmeId}")
    public ResponseEntity<?> getReviewsByFilme(@PathVariable int filmeId) {
        try {
            List<Review> reviews = reviewService.buscarPorFilme(filmeId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Exclui uma review pelo seu ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable int id) {
        try {
            return reviewService.deletarReview(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

