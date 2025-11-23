package com.example.tpaedsiii.service;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tpaedsiii.repository.bd.compressao.Huffman;

@Service
public class HuffmanCompressionService {

    private final File dataDir = new File("data");

    public byte[] createHuffmanArchive() throws Exception {
        if (!dataDir.exists() || !dataDir.isDirectory()) {
            throw new IllegalStateException("Diretório de dados não encontrado: " + dataDir.getAbsolutePath());
        }

        // Gera os dados originais (container)
        byte[] originalData = gerarContainer();

        // Comprime os dados com SEU algoritmo Huffman
        byte[] compressedData = Huffman.comprimirBytes(originalData);

        // ==========================
        // 🔥 Cálculo automático
        // ==========================
        double originalSize = originalData.length;
        double compressedSize = compressedData.length;

        double ratio = 1 - (compressedSize / originalSize);

        System.out.println("\n=== TAXA DE COMPRESSÃO HUFFMAN ===");
        System.out.println("Tamanho original:    " + originalSize + " bytes");
        System.out.println("Tamanho comprimido:  " + compressedSize + " bytes");
        System.out.println("Taxa de compressão:  " + String.format("%.2f", ratio * 100) + "%\n");

        return compressedData;
    }

    private byte[] gerarContainer() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        List<File> arquivos = listarArquivos(dataDir);

        dos.writeInt(arquivos.size());

        for (File f : arquivos) {
            String relative = getRelativePath(dataDir, f);
            byte[] nomeBytes = relative.getBytes(StandardCharsets.UTF_8);

            dos.writeInt(nomeBytes.length);
            dos.write(nomeBytes);

            long tamanho = f.length();
            dos.writeLong(tamanho);

            try (InputStream in = new FileInputStream(f)) {
                byte[] buffer = new byte[8192];
                int l;
                while ((l = in.read(buffer)) != -1) {
                    dos.write(buffer, 0, l);
                }
            }
        }

        dos.flush();
        return baos.toByteArray();
    }

    private List<File> listarArquivos(File dir) {
        List<File> list = new ArrayList<>();
        File[] arr = dir.listFiles();
        if (arr == null) return list;

        for (File f : arr) {
            if (f.isDirectory()) list.addAll(listarArquivos(f));
            else list.add(f);
        }

        return list;
    }

    private String getRelativePath(File base, File file) {
        String basePath = base.getAbsoluteFile().toURI().getPath();
        String filePath = file.getAbsoluteFile().toURI().getPath();
        String rel = filePath.substring(basePath.length());
        if (rel.startsWith("/")) rel = rel.substring(1);
        return rel;
    }
}
