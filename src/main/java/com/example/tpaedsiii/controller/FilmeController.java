package com.example.tpaedsiii.controller;

import com.example.tpaedsiii.model.filme.Filme;
import com.example.tpaedsiii.service.FilmeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/filmes")
@Tag(name = "Filmes", description = "Endpoints para o gerenciamento de filmes")
@CrossOrigin(origins = "*")
public class FilmeController {

    private final FilmeService filmeService;

    public FilmeController(FilmeService filmeService) {
        this.filmeService = filmeService;
    }

    @Operation(summary = "Busca um filme pelo seu ID único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Filme encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Filme não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getFilmeById(@PathVariable int id) {
        try {
            Filme filme = filmeService.buscarPorId(id);
            return filme != null ? ResponseEntity.ok(filme) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Busca filmes cujo título contenha um determinado termo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso (pode retornar uma lista vazia)")
    })
    @GetMapping("/buscar")
    public ResponseEntity<?> searchFilmesByTitle(@RequestParam String titulo) {
        try {
            List<Filme> filmes = filmeService.buscarPorTitulo(titulo);
            return ResponseEntity.ok(filmes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Cria um novo filme no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Filme criado com sucesso")
    })
    @PostMapping
    public ResponseEntity<?> createFilme(@RequestBody Filme filme) {
        try {
            int novoId = filmeService.criarFilme(filme);
            filme.setId(novoId);
            return ResponseEntity.created(URI.create("/api/filmes/" + novoId)).body(filme);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    @Operation(summary = "Exclui um filme pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Filme excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Filme não encontrado para exclusão")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilme(@PathVariable int id) {
        try {
            return filmeService.deletarFilme(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}