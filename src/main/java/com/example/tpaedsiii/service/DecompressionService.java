package com.example.tpaedsiii.service;

import com.example.tpaedsiii.repository.bd.compressao.LZW;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class DecompressionService {

    private final File dataDir = new File("data");
    private final File defaultBackup = new File(dataDir, "db_backup.lzw");

    public void decompressLzwToDirectory(byte[] lzwBytes, File outputDir) throws Exception {
        if (outputDir == null) outputDir = new File(dataDir, "restored");
        if (!outputDir.exists()) outputDir.mkdirs();

        byte[] container = LZW.decodifica(lzwBytes);

        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(container))) {
            int count = dis.readInt();
            for (int i = 0; i < count; i++) {
                int nameLen = dis.readInt();
                byte[] nameBytes = new byte[nameLen];
                dis.readFully(nameBytes);
                String relative = new String(nameBytes, StandardCharsets.UTF_8);

                long fileLen = dis.readLong();

                File outFile = new File(outputDir, relative);
                if (outFile.getParentFile() != null) outFile.getParentFile().mkdirs();

                try (FileOutputStream fos = new FileOutputStream(outFile)) {
                    long remaining = fileLen;
                    byte[] buf = new byte[8192];
                    while (remaining > 0) {
                        int toRead = (int) Math.min(buf.length, remaining);
                        int read = dis.read(buf, 0, toRead);
                        if (read == -1) throw new EOFException("Formato inválido do container: EOF precoce");
                        fos.write(buf, 0, read);
                        remaining -= read;
                    }
                }
            }
        }
    }

    public void decompressDefaultBackupToRestored() throws Exception {
        if (!defaultBackup.exists()) throw new FileNotFoundException("Arquivo padrão db_backup.lzw não encontrado em: " + defaultBackup.getAbsolutePath());
        byte[] bytes;
        try (FileInputStream fis = new FileInputStream(defaultBackup);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[8192];
            int r;
            while ((r = fis.read(buf)) != -1) baos.write(buf, 0, r);
            bytes = baos.toByteArray();
        }
        decompressLzwToDirectory(bytes, null);
    }
}
