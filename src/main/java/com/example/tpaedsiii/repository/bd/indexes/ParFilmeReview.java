package com.example.tpaedsiii.repository.bd.indexes;

import com.example.tpaedsiii.repository.bd.indexes.base.RegistroHash;
import java.io.*;
import java.util.Objects;

public class ParFilmeReview implements RegistroHash<ParFilmeReview> {
    private int filmeId;
    private int reviewId;
    private static final int SIZE = 8;

    public ParFilmeReview(int filmeId, int reviewId) { this.filmeId = filmeId; this.reviewId = reviewId; }
    public ParFilmeReview() { this(-1, -1); }
    public int getFilmeId() { return filmeId; }
    public int getReviewId() { return reviewId; }

    @Override public int size() { return SIZE; }

    /**
     * CORRIGIDO: Implementação com corpo VAZIO para satisfazer a interface.
     * O ID gerado pelo HashExtensivel.create é irrelevante e simplesmente ignorado.
     */
    @Override
    public void setId(int id) {
        // Não faz nada. O corpo está intencionalmente vazio.
    }

    @Override public int hashCode() { return this.filmeId; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParFilmeReview that = (ParFilmeReview) o;
        return filmeId == that.filmeId && reviewId == that.reviewId;
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
}