package com.example.tpaedsiii.controller;

import com.example.tpaedsiii.service.DecompressionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class DecompressionController {

    private final DecompressionService decompressionService;

    @Autowired
    public DecompressionController(DecompressionService decompressionService) {
        this.decompressionService = decompressionService;
    }

    /**
     * Exige um arquivo .lzw enviado no campo "file" (multipart).
     * Retorna um ZIP com os arquivos restaurados.
     */
    @PostMapping(path = "/decompress-db", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = "application/zip")
    public ResponseEntity<byte[]> decompressDb(@RequestPart("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body("Envie um arquivo .lzw no campo 'file'.".getBytes());
            }

            byte[] lzwBytes = file.getBytes();
            byte[] zipBytes = decompressionService.decompressLzwToZipBytes(lzwBytes);

            String filename = "db_restored.zip";

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(zipBytes.length));

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("application/zip"))
                    .body(zipBytes);

        } catch (Exception e) {
            e.printStackTrace();
            String msg = "Erro ao descompactar/gerar ZIP: " + e.getMessage();
            return ResponseEntity.status(500).contentType(MediaType.TEXT_PLAIN).body(msg.getBytes());
        }
    }
}
