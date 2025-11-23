package com.example.tpaedsiii.controller;

import com.example.tpaedsiii.service.CompressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompressionController {

    private final CompressionService compressionService;

    @Autowired
    public CompressionController(CompressionService compressionService) {
        this.compressionService = compressionService;
    }

    @PostMapping(path = "/api/compress-db", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> compressDb() {
        try {
            byte[] compressed = compressionService.createLzwArchive();
            String filename = "db_backup.lzw";

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(compressed.length));

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(compressed);
        } catch (Exception e) {
            e.printStackTrace();
            String msg = "Erro ao gerar backup: " + e.getMessage();
            return ResponseEntity.status(500).contentType(MediaType.TEXT_PLAIN).body(msg.getBytes());
        }
    }
}
