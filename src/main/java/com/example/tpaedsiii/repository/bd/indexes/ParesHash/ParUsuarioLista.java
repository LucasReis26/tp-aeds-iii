package com.example.tpaedsiii.repository.bd.indexes;

import com.example.tpaedsiii.repository.bd.indexes.base.RegistroHash;
import java.io.*;

public class ParUsuarioLista implements RegistroHash<ParUsuarioLista> {
    private int userId;
    private int listaId;
    private static final int SIZE = 8;

    public ParUsuarioLista(int userId, int listaId) { this.userId = userId; this.listaId = listaId; }
    public ParUsuarioLista() { this(-1, -1); }
    public int getUserId() { return userId; }
    public int getListaId() { return listaId; }

    @Override public int size() { return SIZE; }

    @Override
    public void setId(int id) {
        // Não faz nada. O corpo está intencionalmente vazio.
    }

    @Override
    public int hashCode() { return this.userId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParUsuarioLista that = (ParUsuarioLista) o;
        return userId == that.userId && listaId == that.listaId;
    }

    @Override
    public byte[] toByteArray() throws IOException { 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.userId);
        dos.writeInt(this.listaId);
        return baos.toByteArray();
     }
    @Override
    public void fromByteArray(byte[] ba) throws IOException { 
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.userId = dis.readInt();
        this.listaId = dis.readInt();
    }
}