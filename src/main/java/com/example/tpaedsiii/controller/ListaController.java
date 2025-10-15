package com.example.tpaedsiii.controller;

import com.example.tpaedsiii.model.lista.Lista; 
import com.example.tpaedsiii.service.ListaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/listas")
@Tag(name = "Listas", description = "Endpoints para gerenciamento de listas de filmes dos usuários")
@CrossOrigin(origins = "*")
public class ListaController {

    private final ListaService listaService;

    @Autowired
    public ListaController(ListaService listaService) {
        this.listaService = listaService;
    }

    @Operation(summary = "Cria as listas padrão ('Favoritos', etc.) para um usuário, se ainda não existirem")
    @PostMapping("/user/{userId}/init-padrao")
    public ResponseEntity<?> initializeDefaultLists(@PathVariable int userId) {
        try {
            listaService.criarListasPadraoParaUsuario(userId);
            return ResponseEntity.ok().body("Listas padrão criadas/verificadas para o usuário " + userId);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Cria uma nova lista personalizada para um usuário")
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

    @Operation(summary = "Adiciona um filme a uma lista existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Filme adicionado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Lista ou filme não encontrado")
    })
    @PostMapping("/{listaId}/filmes/{filmeId}")
    public ResponseEntity<?> addFilmeToList(@PathVariable int listaId, @PathVariable int filmeId) {
        try {
            listaService.adicionarFilme(listaId, filmeId);
            return ResponseEntity.ok().build(); 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Remove um filme de uma lista")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Filme removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Relação entre lista e filme não encontrada")
    })
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

    @Operation(summary = "Atualiza o nome de uma lista pelo seu ID")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateList(@PathVariable int id, @RequestBody Lista listaData) {
        try {
            Lista updatedList = listaService.atualizarLista(id, listaData.getNome());
            return ResponseEntity.ok(updatedList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Busca todas as listas de um usuário, incluindo os filmes de cada uma")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getListsByUser(@PathVariable int userId) {
        try {
            List<Lista> listas = listaService.buscarPorUsuario(userId);
            return ResponseEntity.ok(listas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Lista todas as listas cadastradas no sistema")
    @GetMapping
    public ResponseEntity<?> getAllLists() {
        try {
            List<Lista> listas = listaService.buscarTodas();
            return ResponseEntity.ok(listas);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @Operation(summary = "Exclui uma lista pelo seu ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable int id) {
        try {
            return listaService.deletarLista(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

