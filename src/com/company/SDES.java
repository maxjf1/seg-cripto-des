package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Implemente S-DES, versão simplificada do DES
 *
 * @author marakaido
 */
public class SDES {
    /**
     * Gera um texto encriptado
     *
     * Cada caractere e considerado um bloco
     */
    public static String encrypt(String msg, int key) {
        // Obtem as duas chaves
        int[] keys = getKeys(key);
        StringBuilder builder = new StringBuilder(msg.length());
        for (int i = 0; i < msg.length(); i++)
            // Encripta blocos de 8 bits
            builder.append((char) encrypt(msg.charAt(i), keys));

        return builder.toString();
    }

    /**
     * Desencripta um texto
     */
    public static String decrypt(String msg, int key) {
        // Obtem as chaves
        int[] keys = getKeys(key);
        StringBuilder builder = new StringBuilder(msg.length());
        for (int i = 0; i < msg.length(); i++)
            // Desencripta blocos
            builder.append((char) decrypt(msg.charAt(i), keys));

        return builder.toString();
    }

    /**
     * Encripta um bloco
     */
    static int encrypt(int c, int[] keys) {
        // Executa a permutação inicial no bloco, e função de permutação usando chave 1
        int result = f(IP(c), keys[0]);
        result = (result << 28) >>> 24 | (result >>> 4);
        // Executa função de permutação com a chave 2
        result = f(result, keys[1]);
        // executa a permutação inicial inversa
        return inverseIP(result);
    }

    static int decrypt(int c, int[] keys) {
        // Inverte a posição das chaves
        int[] newKeys = new int[2];
        newKeys[0] = keys[1];
        newKeys[1] = keys[0];
        // Executa o mesmo algoritmo com chave inversa
        return encrypt(c, newKeys);
    }

    /**
     * Divide o bloco em lado direito e esquerdo e executa função de permutação.
     */
    static int f(int plainText, int subKey) {
        int L = plainText >>> 4;
        int R = plainText << 28 >>> 28;
        return (L ^ F(R, subKey)) << 4 | R;
    }

    /**
     * Permutação simples de 10 bits
     */
    static int P10(int key) {
        return permutate(key, 4, 2, 1, 9, 0, 6, 3, 8, 5, 7);
    }

    /**
     * Left Shift
     */
    static int LS(int key) {
        return permutate(key, 4, 0, 1, 2, 3, 9, 5, 6, 7, 8);
    }


    /**
     * Permutação simples de 8 bits
     */
    static int P8(int key) {
        return permutate(key, 1, 0, 5, 2, 6, 3, 7, 4);
    }

    /**
     * Gera duas chaves a partir da principal
     */
    static int[] getKeys(int key) {
        int[] result = new int[2];
        // Executa uma permutação de 10 bits na chave e um Left Shift
        int shifted = LS(P10(key));
        // Executa permutação de 8 bits e salva como chave 1
        result[0] = P8(shifted);
        // Executa um Left Shift
        shifted = LS(shifted);
        // Executa uma permutação de 8 bits e salva como chave
        result[1] = P8(shifted);
        return result;
    }

    /**
     * Permutação inicial
     */
    static int IP(int plainText) {
        return permutate(plainText, 1, 3, 0, 4, 7, 5, 2, 6);
    }

    /**
     * Permutação inicial inversa
     */
    static int inverseIP(int cryptoText) {
        return permutate(cryptoText, 2, 0, 6, 1, 3, 5, 7, 4);
    }

    /**
     * Executa algoritmo de permutação
     */
    static int permutate(int bits, int... pos) {
        int permutatedBits = 0;
        for (int i = 0; i < pos.length; i++)
            permutatedBits |= ((bits >>> pos[i]) & 1) << i;
        return permutatedBits;
    }

    /**
     * Função principal para permutar lados do bloco com chave de criptografia
     */
    static int F(int plainText, int subKey) {
        // Bloco de 4 bits
        int permutation = permutate(plainText, 3, 0, 1, 2, 1, 2, 3, 0);
        // XOR
        permutation ^= subKey;
        // Permutação com S-Box
        int substituted = 0;
        int i = ((permutation & (1 << 7)) >>> 6) | (permutation & (1 << 4)) >>> 4;
        int j = ((permutation & (1 << 6)) >>> 5) | (permutation & (1 << 5)) >>> 5;
        substituted |= S0[i][j] << 2;
        i = ((permutation & (1 << 3)) >>> 2) | (permutation & 1);
        j = ((permutation & (1 << 2)) >>> 1) | (permutation & (1 << 1)) >>> 1;
        substituted |= S1[i][j];

        return permutate(substituted, 3, 1, 0, 2);
    }

    private final static int[][] S0 = new int[][]{
            {1, 0, 3, 2},
            {3, 2, 1, 0},
            {0, 2, 1, 3},
            {3, 1, 3, 1}
    };

    private final static int[][] S1 = new int[][]{
            {1, 1, 2, 3},
            {2, 0, 1, 3},
            {3, 0, 1, 0},
            {2, 1, 0, 3}
    };
}