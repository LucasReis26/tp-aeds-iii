package com.example.tpaedsiii.repository.bd.search;

import java.util.ArrayList;
import java.util.List;

public class KMP {

    /**
     * Calcula o Longest Proper Prefix which is also a Suffix (LPS array) para o padrão.
     */
    private static int[] computeLPSArray(String pattern) {
        int M = pattern.length();
        int[] lps = new int[M];
        int len = 0; // length of the previous longest prefix suffix
        int i = 1;

        lps[0] = 0; // lps[0] is always 0

        while (i < M) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1]; // Não incrementa i
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    /**
     * Implementa o algoritmo KMP para encontrar todas as ocorrências do padrão no texto.
     * @param text O texto onde buscar (ex: título do filme).
     * @param pattern O padrão a ser buscado.
     * @return Uma lista de índices de início das ocorrências.
     */
    public static List<Integer> search(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        int N = text.length();
        int M = pattern.length();

        if (M == 0) return occurrences;
        if (N == 0 || M > N) return occurrences;

        int[] lps = computeLPSArray(pattern);
        int i = 0; // index for text
        int j = 0; // index for pattern

        while (i < N) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }

            if (j == M) {
                // Ocorrência encontrada
                occurrences.add(i - j);
                // Reseta j para o próximo casamento, usando a tabela LPS
                j = lps[j - 1];
            } else if (i < N && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    // Slide o padrão com a ajuda do LPS
                    j = lps[j - 1];
                } else {
                    // Não há prefixo/sufixo para reuso, move para o próximo caractere no texto
                    i++;
                }
            }
        }
        return occurrences;
    }
}
