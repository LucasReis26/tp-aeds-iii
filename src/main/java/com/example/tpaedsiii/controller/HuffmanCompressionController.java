package com.example.tpaedsiii.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tpaedsiii.service.HuffmanCompressionService;

@RestController
public class HuffmanCompressionController {

    private final HuffmanCompressionService compressionService;

    @Autowired
    public HuffmanCompressionController(HuffmanCompressionService compressionService) {
        this.compressionService = compressionService;
    }

    @PostMapping(path = "/api/compress-db-huffman", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> compressDbHuffman() {
        try {
            byte[] compressed = compressionService.createHuffmanArchive();
            String filename = "db_backup.huf";

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(compressed.length));

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(compressed);

        } catch (Exception e) {
            e.printStackTrace();
            String msg = "Erro ao gerar backup Huffman: " + e.getMessage();
            return ResponseEntity.status(500)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(msg.getBytes());
        }
    }
}
