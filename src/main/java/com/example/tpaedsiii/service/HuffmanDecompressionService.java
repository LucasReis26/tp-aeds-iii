package com.example.tpaedsiii.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Service;

import com.example.tpaedsiii.repository.bd.compressao.Huffman;

@Service
public class HuffmanDecompressionService {

    private final File dataDir = new File("data");
    private final File defaultBackup = new File(dataDir, "db_backup.huff");

    /**
     * Agora retorna um ZIP contendo todos os arquivos restaurados.
     */
    public byte[] decompressHuffmanToZip(byte[] huffBytes) throws Exception {

        // Decodifica usando seu algoritmo Huffman
        byte[] container = Huffman.descomprimirBytes(huffBytes);

        // Converte o container restaurado para ZIP
        return gerarZip(container);
    }

    /**
     * Lê o "container" restaurado e transforma tudo em um ZIP.
     */
    private byte[] gerarZip(byte[] container) throws Exception {

        ByteArrayOutputStream zipBytes = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(zipBytes);

        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(container))) {

            int count = dis.readInt();

            for (int i = 0; i < count; i++) {

                int nameLen = dis.readInt();
                byte[] nameBytes = new byte[nameLen];
                dis.readFully(nameBytes);

                String relative = new String(nameBytes, StandardCharsets.UTF_8);

                long fileLen = dis.readLong();

                byte[] fileContent = new byte[(int) fileLen];
                dis.readFully(fileContent);

                // Adiciona ao ZIP
                ZipEntry entry = new ZipEntry(relative);
                zipOut.putNextEntry(entry);
                zipOut.write(fileContent);
                zipOut.closeEntry();
            }
        }

        zipOut.close();
        return zipBytes.toByteArray();
    }

    /**
     * Para quando não for enviado arquivo
     */
    public byte[] decompressDefaultBackupToZip() throws Exception {
        if (!defaultBackup.exists())
            throw new FileNotFoundException("Arquivo db_backup.huff não encontrado!");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (FileInputStream fis = new FileInputStream(defaultBackup)) {
            byte[] buffer = new byte[8192];
            int r;
            while ((r = fis.read(buffer)) != -1)
                baos.write(buffer, 0, r);
        }

        return decompressHuffmanToZip(baos.toByteArray());
    }
}
