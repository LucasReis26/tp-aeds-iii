package com.example.tpaedsiii.repository.bd.indexes.ParesArvoreB;

import com.example.tpaedsiii.repository.bd.indexes.base.RegistroArvoreBMais;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ParIntString implements RegistroArvoreBMais<ParIntString> {

    private int chaveInt;       
    private String chaveString; 
    private int valor;          

    private static final short TAMANHO_STRING = 64;
    private static final short SIZE = (TAMANHO_STRING*2) + 4; 

    public ParIntString(int chaveInt, String chaveString, int valor) {
        this.chaveInt = chaveInt;
        this.chaveString = chaveString;
        this.valor = valor;
    }

    public ParIntString() {
        this(-1, "", -1);
    }

    public int getChaveInt() { return chaveInt; }
    public String getChaveString() { return chaveString; }
    public int getValor() { return valor; }

    @Override
    public short size() {
        return SIZE;
    }

    @Override
    public ParIntString clone() {
        return new ParIntString(this.chaveInt, this.chaveString, this.valor);
    }


    @Override
    public int compareTo(ParIntString outro) {
        int diffInt = Integer.compare(this.chaveInt, outro.chaveInt);
        if (diffInt != 0) {
            return diffInt;
        }
        return this.chaveString.compareTo(outro.chaveString);
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(SIZE);
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.chaveInt);

        byte[] stringBytes = this.chaveString.getBytes(StandardCharsets.UTF_8);
        if (stringBytes.length > TAMANHO_STRING) {
            throw new IOException("A chave de string excede o tamanho máximo de " + TAMANHO_STRING + " bytes.");
        }
        dos.write(stringBytes);
        for (int i = stringBytes.length; i < TAMANHO_STRING; i++) {
            dos.writeByte(0);
        }

        dos.writeInt(this.valor);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        this.chaveInt = dis.readInt();

        byte[] stringBytes = new byte[TAMANHO_STRING];
        dis.read(stringBytes);

        this.valor = dis.readInt();

        int len = 0;
        while (len < stringBytes.length && stringBytes[len] != 0) {
            len++;
        }
        this.chaveString = new String(stringBytes, 0, len, StandardCharsets.UTF_8);
    }

    @Override
    public String toString() {
        return "Idx[Chave:" + chaveInt + ", Ordenação:'" + chaveString + "'=>Valor:" + valor + "]";
    }
}

