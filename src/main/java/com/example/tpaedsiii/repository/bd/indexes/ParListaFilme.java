package com.example.tpaedsiii.repository.bd.indexes;

import com.example.tpaedsiii.repository.bd.indexes.base.RegistroHash;
import java.io.*;
import java.util.Objects;

public class ParListaFilme implements RegistroHash<ParListaFilme> {
    private int listaId;
    private int filmeId;
    private static final int SIZE = 8;

    public ParListaFilme(int listaId, int filmeId) { this.listaId = listaId; this.filmeId = filmeId; }
    public ParListaFilme() { this(-1, -1); }
    public int getListaId() { return listaId; }
    public int getFilmeId() { return filmeId; }

    @Override public int size() { return SIZE; }

    @Override
    public void setId(int id) {
        // Não faz nada. O corpo está intencionalmente vazio.
    }

    @Override public int hashCode() { return this.listaId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParListaFilme that = (ParListaFilme) o;
        return listaId == that.listaId && filmeId == that.filmeId;
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
}