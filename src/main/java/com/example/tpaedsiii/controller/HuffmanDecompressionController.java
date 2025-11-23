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
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<byte[]> decompressDbHuffman(
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            byte[] zipBytes;

            if (file != null && !file.isEmpty()) {
                zipBytes = decompressionService.decompressHuffmanToZip(file.getBytes());
            } else {
                zipBytes = decompressionService.decompressDefaultBackupToZip();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"restore_huffman.zip\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(zipBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(("Erro ao descompactar Huffman: " + e.getMessage()).getBytes());
        }
    }
}
