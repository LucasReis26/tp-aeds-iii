package com.example.tpaedsiii.service;

import com.example.tpaedsiii.repository.bd.compressao.LZW;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompressionService {

    private final File dataDir = new File("data");

    public byte[] createLzwArchive() throws Exception {
        if (!dataDir.exists() || !dataDir.isDirectory()) {
            throw new IllegalStateException("Diretório de dados não encontrado: " + dataDir.getAbsolutePath());
        }

        byte[] archiveBytes;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(baos)) {

            List<File> files = listFilesRecursively(dataDir);
            dos.writeInt(files.size());
            for (File f : files) {
                String relative = getRelativePath(dataDir, f);
                byte[] nameBytes = relative.getBytes(StandardCharsets.UTF_8);
                dos.writeInt(nameBytes.length);
                dos.write(nameBytes);
                long fileLen = f.length();
                dos.writeLong(fileLen);

                try (FileInputStream fis = new FileInputStream(f)) {
                    byte[] buf = new byte[8192];
                    int r;
                    while ((r = fis.read(buf)) != -1) dos.write(buf, 0, r);
                }
            }
            dos.flush();
            archiveBytes = baos.toByteArray();
        }

        // chama seu LZW (do package repository.bd.compressao)
        byte[] compressed = LZW.codifica(archiveBytes);
        return compressed;
    }

    private List<File> listFilesRecursively(File dir) {
        List<File> files = new ArrayList<>();
        File[] entries = dir.listFiles();
        if (entries == null) return files;
        for (File f : entries) {
            if (f.isDirectory()) files.addAll(listFilesRecursively(f));
            else files.add(f);
        }
        return files;
    }

    private String getRelativePath(File base, File file) {
        String basePath = base.getAbsoluteFile().toURI().getPath();
        String filePath = file.getAbsoluteFile().toURI().getPath();
        String rel = filePath.substring(basePath.length());
        if (rel.startsWith("/")) rel = rel.substring(1);
        return rel;
    }
    
}
