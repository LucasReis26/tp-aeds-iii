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

    @Operation(summary = "Lista todos os filmes cadastrados")
    @GetMapping
    public ResponseEntity<?> getAllFilmes() {
        try {
            List<Filme> filmes = filmeService.listarTodos();
            return ResponseEntity.ok(filmes);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @Operation(summary = "Busca um filme pelo seu ID único")
    @GetMapping("/{id}")
    public ResponseEntity<?> getFilmeById(@PathVariable int id) {
        try {
            Filme filme = filmeService.buscarPorId(id);
            return ResponseEntity.ok(filme);
        } catch (Exception e) {
            if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(404).body(e.getMessage());
            } else {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }

    @Operation(summary = "Busca filmes cujo título contenha um determinado termo")
    @GetMapping("/buscar")
    public ResponseEntity<?> searchFilmesByTitle(@RequestParam String titulo) {
        try {
            List<Filme> filmes = filmeService.buscarPorTitulo(titulo);
            return ResponseEntity.ok(filmes);
        } catch (Exception e) {
            if (e.getMessage().contains("Nenhum filme encontrado")) {
                return ResponseEntity.status(404).body(e.getMessage());
            } else {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }

    @Operation(summary = "Cria um novo filme no sistema")
    @PostMapping
    public ResponseEntity<?> createFilme(@RequestBody Filme filme) {
        try {
            int novoId = filmeService.criarFilme(filme);
            filme.setId(novoId);
            return ResponseEntity.created(URI.create("/api/filmes/" + novoId)).body(filme);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Atualiza um filme existente")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFilme(@PathVariable int id, @RequestBody Filme filme) {
        try {
            filme.setId(id);
            filmeService.atualizarFilme(filme);
            return ResponseEntity.ok(filme);
        } catch (Exception e) {
            if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(404).body(e.getMessage());
            } else {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }
    
    @Operation(summary = "Exclui um filme pelo seu ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFilme(@PathVariable int id) {
        try {
            filmeService.deletarFilme(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            if (e.getMessage().contains("não encontrado")) {
                return ResponseEntity.status(404).body(e.getMessage());
            } else {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }
}