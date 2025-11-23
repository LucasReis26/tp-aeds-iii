package com.example.tpaedsiii.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

import com.example.tpaedsiii.repository.bd.compressao.Huffman;

@Service
public class HuffmanDecompressionService {

    private final File dataDir = new File("data");
    private final File defaultBackup = new File(dataDir, "db_backup.huff");

    public void decompressHuffmanToDirectory(byte[] huffBytes, File outputDir) throws Exception {

        if (outputDir == null)
            outputDir = new File(dataDir, "restored_huffman");

        if (!outputDir.exists())
            outputDir.mkdirs();

        // Decodifica usando SEU Huffman
        byte[] container = Huffman.descomprimirBytes(huffBytes);

        restaurarArquivos(container, outputDir);
    }

    private void restaurarArquivos(byte[] container, File outputDir) throws Exception {

        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(container))) {

            int count = dis.readInt();

            for (int i = 0; i < count; i++) {

                int nameLen = dis.readInt();
                byte[] nameBytes = new byte[nameLen];
                dis.readFully(nameBytes);

                String relative = new String(nameBytes, StandardCharsets.UTF_8);

                long fileLen = dis.readLong();

                File outFile = new File(outputDir, relative);

                if (outFile.getParentFile() != null)
                    outFile.getParentFile().mkdirs();

                try (FileOutputStream fos = new FileOutputStream(outFile)) {
                    byte[] buffer = new byte[8192];
                    long remaining = fileLen;

                    while (remaining > 0) {
                        int toRead = (int) Math.min(buffer.length, remaining);
                        int read = dis.read(buffer, 0, toRead);
                        if (read == -1) throw new EOFException("Container truncado.");
                        fos.write(buffer, 0, read);
                        remaining -= read;
                    }
                }
            }
        }
    }

    public void decompressDefaultBackupToRestored() throws Exception {
        if (!defaultBackup.exists())
            throw new FileNotFoundException("Arquivo db_backup.huff não encontrado!");

        byte[] bytes;
        try (FileInputStream fis = new FileInputStream(defaultBackup);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            byte[] b = new byte[8192];
            int r;
            while ((r = fis.read(b)) != -1)
                baos.write(b, 0, r);

            bytes = baos.toByteArray();
        }

        decompressHuffmanToDirectory(bytes, null);
    }
}
