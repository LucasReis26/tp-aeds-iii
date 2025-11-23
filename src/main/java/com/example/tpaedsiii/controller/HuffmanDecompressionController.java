package com.example.tpaedsiii.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.tpaedsiii.service.HuffmanDecompressionService;

@RestController
public class HuffmanDecompressionController {

    private final HuffmanDecompressionService decompressionService;

    @Autowired
    public HuffmanDecompressionController(HuffmanDecompressionService decompressionService) {
        this.decompressionService = decompressionService;
    }

    @PostMapping(
        path = "/api/decompress-db-huffman",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> decompressDbHuffman(
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            String message;

            if (file != null && !file.isEmpty()) {
                byte[] bytes = file.getBytes();
                decompressionService.decompressHuffmanToDirectory(bytes, null);
                message = "Descompactação Huffman concluída (arquivo enviado).";
            } else {
                decompressionService.decompressDefaultBackupToRestored(); // <-- CORRIGIDO
                message = "Descompactação Huffman concluída (usando data/db_backup.huff).";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(message);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Erro ao descompactar Huffman: " + e.getMessage());
        }
    }
}
