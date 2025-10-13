package com.example.tpaedsiii.repository.bd.indexes;

import com.example.tpaedsiii.repository.bd.indexes.base.RegistroHash;
import java.io.*;

/**
 * Representa a ligação (ponteiro) de um Filme para uma de suas Reviews.
 * É a "ficha" do índice que responde: "Quais reviews são do filme Y?".
 */
public class ParFilmeReview implements RegistroHash<ParFilmeReview> {
    private int filmeId;
    private int reviewId;
    private static final int SIZE = 8;

    public ParFilmeReview(int filmeId, int reviewId) {
        this.filmeId = filmeId;
        this.reviewId = reviewId;
    }

    public ParFilmeReview() {
        this(-1, -1);
    }

    public int getFilmeId() { return filmeId; }
    public int getReviewId() { return reviewId; }

    @Override
    public int hashCode() {
        // A chave do índice é o filmeId.
        return this.filmeId;
    }

    @Override
    public int size() {
        return SIZE;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.filmeId);
        dos.writeInt(this.reviewId);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.filmeId = dis.readInt();
        this.reviewId = dis.readInt();
    }

    @Override
    public void setId(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setId'");
    }
}