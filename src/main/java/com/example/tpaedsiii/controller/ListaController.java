package com.example.tpaedsiii.controller;

import com.example.tpaedsiii.model.lista.Lista;
import com.example.tpaedsiii.service.ListaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // IMPORTAÇÃO NECESSÁRIA
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/listas")
public class ListaController {

    private final ListaService listaService;

    @Autowired
    public ListaController(ListaService listaService) {
        this.listaService = listaService;
    }

    @PostMapping("/user/{userId}/init-padrao")
    public ResponseEntity<?> initializeDefaultLists(@PathVariable int userId) {
        try {
            listaService.criarListasPadrao(userId);
            return ResponseEntity.ok().body("Listas padrão criadas/verificadas para o usuário " + userId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createCustomList(@RequestBody Lista lista) {
        try {
            int novoId = listaService.criarListaPersonalizada(lista);
            lista.setId(novoId);
            return ResponseEntity.created(URI.create("/api/listas/" + novoId)).body(lista);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{listaId}/filmes/{filmeId}")
    public ResponseEntity<?> addFilmeToList(@PathVariable int listaId, @PathVariable int filmeId) {
        try {
            listaService.adicionarFilme(listaId, filmeId);
            return ResponseEntity.ok().build(); 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{listaId}/filmes/{filmeId}")
    public ResponseEntity<?> removeFilmeFromList(@PathVariable int listaId, @PathVariable int filmeId) {
        try {
            if (listaService.removerFilme(listaId, filmeId)) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getListsByUser(@PathVariable int userId) {
        try {
            List<Lista> listas = listaService.buscarPorUsuario(userId);
            return ResponseEntity.ok(listas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable int id) {
        try {
            return listaService.deletarLista(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}