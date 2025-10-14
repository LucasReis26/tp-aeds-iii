package com.example.tpaedsiii.repository.bd.indexes;

import com.example.tpaedsiii.repository.bd.indexes.base.RegistroHash;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParUsuarioReview implements RegistroHash<ParUsuarioReview> {
    private int userId;
    private int reviewId;
    private static final int SIZE = 8; 

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
        return this.userId;
    }

    public int size() {
        return SIZE;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.userId);
        dos.writeInt(this.reviewId);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.userId = dis.readInt();
        this.reviewId = dis.readInt();
    }

    public void setId(int id) {
    }
}