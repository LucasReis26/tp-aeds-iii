package com.example.tpaedsiii.service;

import com.example.tpaedsiii.repository.bd.compressao.LZW;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DecompressionService {

    
    public byte[] decompressLzwToZipBytes(byte[] lzwBytes) throws Exception {
        if (lzwBytes == null || lzwBytes.length == 0) {
            throw new IllegalArgumentException("Arquivo LZW vazio.");
        }

        // Decodifica LZW -> obtém o container binário com arquivos
        byte[] container = LZW.decodifica(lzwBytes);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(container);
             DataInputStream dis = new DataInputStream(bais);
             ByteArrayOutputStream baosZip = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baosZip)) {

            int count = dis.readInt();
            for (int i = 0; i < count; i++) {
                int nameLen = dis.readInt();
                byte[] nameBytes = new byte[nameLen];
                dis.readFully(nameBytes);
                String relative = new String(nameBytes, StandardCharsets.UTF_8);

                long fileLen = dis.readLong();

                // Cria entrada de zip com caminho relativo
                ZipEntry entry = new ZipEntry(relative);
                if (relative.endsWith("/")) {
                    entry = new ZipEntry(relative);
                    zos.putNextEntry(entry);
                    zos.closeEntry();
                    continue;
                }

                zos.putNextEntry(entry);

                // Escreve conteúdo do arquivo no zip
                long remaining = fileLen;
                byte[] buf = new byte[8192];
                while (remaining > 0) {
                    int toRead = (int) Math.min(buf.length, remaining);
                    int read = dis.read(buf, 0, toRead);
                    if (read == -1) throw new EOFException("Formato do container inválido: EOF precoce");
                    zos.write(buf, 0, read);
                    remaining -= read;
                }

                zos.closeEntry();
            }

            zos.finish();
            return baosZip.toByteArray();
        }
    }
}
