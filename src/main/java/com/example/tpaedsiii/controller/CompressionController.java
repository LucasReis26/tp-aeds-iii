package com.example.tpaedsiii.controller;

import com.example.tpaedsiii.service.CompressionService;
import com.example.tpaedsiii.service.CompressionService.CompressionResult;
import com.example.tpaedsiii.service.CompressionService.CompressionStats;

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
            CompressionResult result = compressionService.createLzwArchiveWithStats();

            byte[] compressed = result.getCompressedBytes();
            CompressionStats stats = result.getStats();

            String filename = "db_backup.lzw";

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(compressed.length));

            // envia stats via headers (igual ao Huffman)
            headers.add("X-Original-Size", String.valueOf((long) stats.getOriginalSize()));
            headers.add("X-Compressed-Size", String.valueOf((long) stats.getCompressedSize()));
            headers.add("X-Compression-Ratio", String.format("%.2f", stats.getRatio() * 100.0)); // em %

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(compressed);

        } catch (Exception e) {
            e.printStackTrace();
            String msg = "Erro ao gerar backup LZW: " + e.getMessage();
            return ResponseEntity.status(500)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(msg.getBytes());
        }
    }
}
