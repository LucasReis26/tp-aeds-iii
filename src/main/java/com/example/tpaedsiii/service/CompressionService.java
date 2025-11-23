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

import com.example.tpaedsiii.repository.bd.compressao.LZW;

@Service
public class CompressionService {

    private final File dataDir = new File("data");

    // Classe que contem as estatísticas
    public static class CompressionStats {
        private final double originalSize;
        private final double compressedSize;
        private final double ratio; // entre 0 e 1

        public CompressionStats(double originalSize, double compressedSize, double ratio) {
            this.originalSize = originalSize;
            this.compressedSize = compressedSize;
            this.ratio = ratio;
        }

        public double getOriginalSize() { return originalSize; }
        public double getCompressedSize() { return compressedSize; }
        public double getRatio() { return ratio; }
    }

    // Resultado contendo bytes comprimidos e stats
    public static class CompressionResult {
        private final byte[] compressedBytes;
        private final CompressionStats stats;

        public CompressionResult(byte[] compressedBytes, CompressionStats stats) {
            this.compressedBytes = compressedBytes;
            this.stats = stats;
        }

        public byte[] getCompressedBytes() { return compressedBytes; }
        public CompressionStats getStats() { return stats; }
    }

    /**
     * Gera o container (lista de arquivos) e comprime com LZW.
     * @return CompressionResult com bytes comprimidos e estatísticas
     * @throws Exception
     */
    public CompressionResult createLzwArchiveWithStats() throws Exception {
        if (!dataDir.exists() || !dataDir.isDirectory()) {
            throw new IllegalStateException("Diretório de dados não encontrado: " + dataDir.getAbsolutePath());
        }

        // Gera container binário (nome relativo + tamanho + conteúdo)
        byte[] originalData = gerarContainer();

        // Comprime com LZW (usa sua classe existente)
        byte[] compressedData = LZW.codifica(originalData);

        // Estatísticas
        double originalSize = originalData.length;
        double compressedSize = compressedData.length;
        double ratio = 0.0;
        if (originalSize > 0) ratio = 1.0 - (compressedSize / originalSize);

        CompressionStats stats = new CompressionStats(originalSize, compressedSize, ratio);

        // Retorna bytes + stats
        return new CompressionResult(compressedData, stats);
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
