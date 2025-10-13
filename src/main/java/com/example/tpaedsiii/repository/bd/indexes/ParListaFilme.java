package com.example.tpaedsiii.repository.bd.indexes;

import com.example.tpaedsiii.repository.bd.indexes.base.RegistroHash;
import java.io.*;

public class ParListaFilme implements RegistroHash<ParListaFilme> {
    private int listaId;
    private int filmeId;
    private static final int SIZE = 8;

    public ParListaFilme(int listaId, int filmeId) {
        this.listaId = listaId;
        this.filmeId = filmeId;
    }

    public ParListaFilme() {
        this(-1, -1);
    }

    public int getListaId() { return listaId; }
    public int getFilmeId() { return filmeId; }

    @Override
    public int hashCode() {
        return this.listaId;
    }

    @Override
    public int size() {
        return SIZE;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.listaId);
        dos.writeInt(this.filmeId);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.listaId = dis.readInt();
        this.filmeId = dis.readInt();
    }

    @Override
    public void setId(int id) {
        throw new UnsupportedOperationException("Unimplemented method 'setId'");
    }
}