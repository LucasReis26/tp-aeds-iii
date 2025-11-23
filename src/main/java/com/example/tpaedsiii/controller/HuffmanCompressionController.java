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

            // ======= NOVO: Executa compressão + coleta métricas =======
            var result = compressionService.createHuffmanArchiveWithStats();

            byte[] compressed = result.getCompressedBytes();
            double originalSize = result.getStats().getOriginalSize();
            double compressedSize = result.getStats().getCompressedSize();
            double ratio = result.getStats().getRatio() * 100;

            String filename = "db_backup.huf";

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(compressed.length));

            // ======= NOVO: envia dados para o HTML através do header =======
            headers.add("X-Original-Size", String.valueOf(originalSize));
            headers.add("X-Compressed-Size", String.valueOf(compressedSize));
            headers.add("X-Compression-Ratio", String.format("%.2f", ratio));

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
