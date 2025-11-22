package com.example.tpaedsiii.extra;

import java.math.BigInteger;

public class RSAEncryptor {
    private BigInteger n;
    private BigInteger e;
    private BigInteger d;

    public RSAEncryptor(int p, int q, int e) {
        this.n = BigInteger.valueOf(p).multiply(BigInteger.valueOf(q));
        this.e = BigInteger.valueOf(e);
        BigInteger phi = BigInteger.valueOf(p - 1).multiply(BigInteger.valueOf(q - 1));
        this.d = this.e.modInverse(phi);
    }

    public String encrypt(String message) {
        if (message == null)
            return null;
        StringBuilder ciphertext = new StringBuilder();

        for (char c : message.toCharArray()) {
            BigInteger m = BigInteger.valueOf((int) c);
            BigInteger crip = m.modPow(e, n);
            ciphertext.append(crip).append(" "); 
        }
        return ciphertext.toString().trim();
    }

    public String decrypt(String ciphertext) {
        if (ciphertext == null || ciphertext.isEmpty())
            return "";
        StringBuilder plaintext = new StringBuilder();

        for (String part : ciphertext.split(" ")) {
            try {
                BigInteger c = new BigInteger(part);
                BigInteger msg = c.modPow(d, n);
                plaintext.append((char) msg.intValue());
            } catch (Exception ex) {
            }
        }
        return plaintext.toString();
    }
}