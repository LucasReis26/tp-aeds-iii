package com.example.tpaedsiii.repository.bd.indexes.ParesArvoreB;

import com.example.tpaedsiii.repository.bd.indexes.base.RegistroArvoreBMais;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ParStringInt implements RegistroArvoreBMais<ParStringInt> {

    private String chave;
    private int valor;     
    private static final short TAMANHO_CHAVE = 128;
    private static final short SIZE = TAMANHO_CHAVE + 4; 

    
    public ParStringInt() {
        this("", -1);
    }

    public ParStringInt(String chave, int valor) {
        this.chave = chave;
        this.valor = valor;
    }

    public String getChave() { return this.chave; }
    public int getValor() { return this.valor; }

    @Override
    public ParStringInt clone() {
        return new ParStringInt(this.chave, this.valor);
    }

    @Override
    public short size() {
        return SIZE;
    }

 
    @Override
    public int compareTo(ParStringInt outro) {
        int diffChave = this.chave.compareTo(outro.chave);
        if (diffChave != 0) {
            return diffChave;
        }
        return Integer.compare(this.valor, outro.valor);
    }

    @Override
    public String toString() {
        return "Idx[Chave:'" + this.chave + "'=>Valor:" + this.valor + "]";
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(SIZE);
        DataOutputStream dos = new DataOutputStream(baos);

        byte[] chaveBytes = this.chave.getBytes(StandardCharsets.UTF_8);
        if (chaveBytes.length > TAMANHO_CHAVE) {
            throw new IOException("A chave de string excede o tamanho máximo de " + TAMANHO_CHAVE + " bytes.");
        }
        
        dos.write(chaveBytes);
        for (int i = chaveBytes.length; i < TAMANHO_CHAVE; i++) {
            dos.writeByte(0);
        }

        dos.writeInt(this.valor);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        byte[] chaveBytes = new byte[TAMANHO_CHAVE];
        dis.read(chaveBytes);
        this.valor = dis.readInt();
        
        int len = 0;
        while (len < chaveBytes.length && chaveBytes[len] != 0) {
            len++;
        }
        this.chave = new String(chaveBytes, 0, len, StandardCharsets.UTF_8);
    }
}

