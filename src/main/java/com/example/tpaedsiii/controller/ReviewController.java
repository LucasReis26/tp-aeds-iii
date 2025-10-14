package com.example.tpaedsiii.controller;

import com.example.tpaedsiii.model.review.Review;
import com.example.tpaedsiii.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "Busca todas as reviews feitas por um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Review excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Review não encontrada para exclusão")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable int id) {
        try {
            return reviewService.deletarReview(id) ? ResponseEntity.noContent().build()
                    : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


}