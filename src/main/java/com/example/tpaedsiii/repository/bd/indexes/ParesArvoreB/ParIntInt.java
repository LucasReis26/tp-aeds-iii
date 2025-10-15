package com.example.tpaedsiii.repository.bd.indexes.ParesArvoreB;

import com.example.tpaedsiii.repository.bd.indexes.base.RegistroArvoreBMais;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParIntInt implements RegistroArvoreBMais<ParIntInt> {

    private int chave;  
    private int valor;  
    private static final short SIZE = 8; 

    
    public ParIntInt() {
        this(0, 0);
    }


    public ParIntInt(int chave, int valor) {
        this.chave = chave;
        this.valor = valor;
    }

    public int getChave() { return this.chave; }
    public int getValor() { return this.valor; }

    @Override
    public ParIntInt clone() {
        return new ParIntInt(this.chave, this.valor);
    }

    @Override
    public short size() {
        return SIZE;
    }

    @Override
    public int compareTo(ParIntInt outro) {
        int diffChave = Integer.compare(this.chave, outro.chave);
        if (diffChave != 0) {
            return diffChave;
        }
        return Integer.compare(this.valor, outro.valor);
    }

    @Override
    public String toString() {
        return "Idx[Chave:" + this.chave + "=>Valor:" + this.valor + "]";
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(SIZE);
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.chave);
        dos.writeInt(this.valor);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.chave = dis.readInt();
        this.valor = dis.readInt();
    }
}
