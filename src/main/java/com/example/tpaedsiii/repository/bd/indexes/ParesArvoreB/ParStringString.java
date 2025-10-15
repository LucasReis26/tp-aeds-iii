package com.example.tpaedsiii.repository.bd.indexes.ParesArvoreB;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.example.tpaedsiii.repository.bd.indexes.base.RegistroArvoreBMais;

public class ParStringString implements RegistroArvoreBMais<ParStringString>{
    private String string1;
    private String string2;
    private static final short TAMANHO_STRING = 128;
    private static final short TAMANHO_Registro = (TAMANHO_STRING * 2) + 4; 

    public ParStringString() {
        this("", "");
    }

    public ParStringString(String string1, String string2) {
        this.string1 = string1;
        this.string2 = string2;
    }

    @Override
    public ParStringString clone() {
        return new ParStringString(this.string1, this.string2);
    }


    @Override
    public short size() {
        return TAMANHO_Registro;
    }

    @Override
      @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(TAMANHO_REGISTO);
        DataOutputStream dos = new DataOutputStream(baos);

        // Escreve a primeira string
        byte[] stringBytes1 = this.string1.getBytes(StandardCharsets.UTF_8);
        if (stringBytes1.length > TAMANHO_STRING) {
            throw new IOException("A string1 excede o tamanho máximo de " + TAMANHO_STRING + " bytes.");
        }
        dos.write(stringBytes1);
        for (int i = stringBytes1.length; i < TAMANHO_STRING; i++) {
            dos.writeByte(0);
        } 


        byte[] stringBytes2 = this.string2.getBytes(StandardCharsets.UTF_8);
        if (stringBytes2.length > TAMANHO_STRING) {
            throw new IOException("A string2 excede o tamanho máximo de " + TAMANHO_STRING + " bytes.");
        }
        dos.write(stringBytes2);
        for (int i = stringBytes2.length; i < TAMANHO_STRING; i++) {
            dos.writeByte(0);
        }

        return baos.toByteArray();
    }

    
    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        byte[] stringBytes = new byte[TAMANHO_STRING];
        dis.read(stringBytes);

        
        byte[] stringBytes2 = new byte[TAMANHO_STRING];
        dis.read(stringBytes2);
        
        int len = 0;
        while (len < stringBytes.length && stringBytes[len] != 0) {
            len++;
        }
        this.string1 = new String(stringBytes, 0, len, StandardCharsets.UTF_8);

        int len2 = 0;
        while (len2 < stringBytes2.length && stringBytes2[len2] != 0) {
            len2++;
        }
        this.string2 = new String(stringBytes2, 0, len2, StandardCharsets.UTF_8);
    }

    @Override
     public int compareTo(ParStringString obj) {
        int diffString1 = this.string1.compareTo(obj.string1);
        if (diffString1 != 0) {
            return diffString1;
        }
        return this.string2.compareTo(obj.string2);
    }
    @Override
    public String toString() {
        return "Idx['" + string1 + "'=>" + string2 + "]";
    }
}
