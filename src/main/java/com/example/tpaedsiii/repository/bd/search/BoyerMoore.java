package com.example.tpaedsiii.repository.bd.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class BoyerMoore {

    // Tamanho do alfabeto ASCII estendido (256 caracteres)
    private static final int ALPHABET_SIZE = 256;

    /**
     * Pré-processa a regra do "bad character" (last occurrence heuristic).
     * Preenche a tabela com o último índice de ocorrência de cada caractere
     * no padrão.
     */
    private static int[] computeBadCharacterShift(String pattern) {
        int M = pattern.length();
        int[] badChar = new int[ALPHABET_SIZE];
        Arrays.fill(badChar, -1); // Inicializa com -1

        // Preenche a tabela com o último índice de ocorrência
        for (int i = 0; i < M; i++) {
            badChar[(int) pattern.charAt(i)] = i;
        }
        return badChar;
    }

    /**
     * Implementa o algoritmo Boyer-Moore (Bad Character Rule).
     * @param text O texto onde buscar.
     * @param pattern O padrão a ser buscado.
     * @return Uma lista de índices de início das ocorrências.
     */
    public static List<Integer> search(String text, String pattern) {
        List<Integer> occurrences = new ArrayList<>();
        int N = text.length();
        int M = pattern.length();
        
        if (M == 0) return occurrences;
        if (N == 0 || M > N) return occurrences;

        int[] badChar = computeBadCharacterShift(pattern);
        int s = 0; // s é o shift do padrão em relação ao texto

        while (s <= (N - M)) {
            int j = M - 1; // Começa a comparação pelo final do padrão

            // Mantém a comparação enquanto os caracteres casam
            while (j >= 0 && pattern.charAt(j) == text.charAt(s + j)) {
                j--;
            }

            // Se o padrão foi encontrado (j < 0)
            if (j < 0) {
                occurrences.add(s);
                // Move o padrão para a próxima posição, garantindo que o último caractere
                // do padrão se alinhe com a próxima ocorrência (se houver, usamos o Good Suffix, 
                // mas aqui simplificamos movendo 1 posição).
                s += (M > 0 ? 1 : 1); // Simplificamos o Good Suffix movendo 1
            } else {
                // Mismatch ocorreu em text[s + j] e pattern[j]
                
                // Encontra a última ocorrência do caractere que causou o mismatch (text[s+j])
                // no padrão. Se não existir, será -1.
                int badCharShift = badChar[text.charAt(s + j)];
                
                // Calcula o deslocamento (shift)
                // É o máximo entre 1 e (j - badCharShift)
                // (j - badCharShift) garante que a última ocorrência do caractere
                // no texto se alinhe no padrão.
                s += Math.max(1, j - badCharShift);
            }
        }
        return occurrences;
    }
}
