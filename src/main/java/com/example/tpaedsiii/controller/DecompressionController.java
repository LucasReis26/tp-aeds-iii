package com.example.tpaedsiii.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.tpaedsiii.service.DecompressionService;

@RestController
public class DecompressionController {

    private final DecompressionService decompressionService;

    @Autowired
    public DecompressionController(DecompressionService decompressionService) {
        this.decompressionService = decompressionService;
    }

    @PostMapping(path = "/api/decompress-db", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> decompressDb(@RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                byte[] bytes = file.getBytes();
                decompressionService.decompressLzwToDirectory(bytes, null);
                return ResponseEntity.ok("Descompactação concluída em data/restored/ (a partir do arquivo enviado).");
            } else {
                decompressionService.decompressDefaultBackupToRestored();
                return ResponseEntity.ok("Descompactação concluída em data/restored/ (a partir de data/db_backup.lzw).");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro na descompactação: " + e.getMessage());
        }
    }
    
}
