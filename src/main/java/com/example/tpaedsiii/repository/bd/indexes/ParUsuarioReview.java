package com.example.tpaedsiii.repository.bd.indexes;

import com.example.tpaedsiii.repository.bd.indexes.base.RegistroHash;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Representa a ligação (ponteiro) de um Usuário para uma de suas Reviews.
 * É a "ficha" do índice que responde: "Quais reviews são do usuário X?".
 */
public class ParUsuarioReview implements RegistroHash<ParUsuarioReview> {
    private int userId;
    private int reviewId;
    private static final int SIZE = 8; // 4 bytes (int) + 4 bytes (int)

    public ParUsuarioReview(int userId, int reviewId) {
        this.userId = userId;
        this.reviewId = reviewId;
    }

    public ParUsuarioReview() {
        this(-1, -1);
    }

    public int getUserId() { return userId; }
    public int getReviewId() { return reviewId; }

    @Override
    public int hashCode() {
        // A chave do índice é o userId. É por este campo que a busca será feita.
        return this.userId;
    }

    @Override
    public int size() {
        return SIZE;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.userId);
        dos.writeInt(this.reviewId);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.userId = dis.readInt();
        this.reviewId = dis.readInt();
    }
}