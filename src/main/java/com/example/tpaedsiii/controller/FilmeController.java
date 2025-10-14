package com.example.tpaedsiii.controller;

import com.example.tpaedsiii.model.filme.Filme;
import com.example.tpaedsiii.service.FilmeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/filmes")
public class FilmeController {

    private final FilmeService filmeService;

    @Autowired
    public FilmeController(FilmeService filmeService) {
        this.filmeService = filmeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFilmeById(@PathVariable int id) {
        try {
            Filme filme = filmeService.buscarPorId(id);
            return filme != null ? ResponseEntity.ok(filme) : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> searchFilmesByTitle(@RequestParam String titulo) {
        try {
            List<Filme> filmes = filmeService.buscarPorTitulo(titulo);
            return ResponseEntity.ok(filmes);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

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
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFilme(@PathVariable int id) {
        try {
            return filmeService.deletarFilme(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}