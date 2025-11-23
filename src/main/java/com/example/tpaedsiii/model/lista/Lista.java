package com.example.tpaedsiii.model.lista;

import com.example.tpaedsiii.repository.bd.base.Registro;
import com.example.tpaedsiii.repository.bd.indexes.base.RegistroArvoreBMais;
import com.example.tpaedsiii.model.filme.Filme;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Lista implements Registro, RegistroArvoreBMais<Lista> {

    private int id;
    private String nome;
    private int userId;
    private boolean isPadrao;
    private ArrayList<Filme> filmes; 
    private static final short TAMANHO_NOME = 64;
    private static final short TAMANHO_FIXO = 4 + 4 + 1 + TAMANHO_NOME; 

    public Lista() {
        this(-1, -1, "", false);
    }
    
    public Lista(int id) {
        this(id, -1, "", false);
    }

    public Lista(int id, int userId, String nome, boolean isPadrao) {
        this.id = id;
        this.userId = userId;
        this.nome = nome;
        this.isPadrao = isPadrao;
        this.filmes = new ArrayList<>();
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public boolean isPadrao() { return isPadrao; }
    public void setPadrao(boolean isPadrao) { this.isPadrao = isPadrao; }
    public ArrayList<Filme> getFilmes() { return filmes; }
    public void setFilmes(ArrayList<Filme> filmes) { this.filmes = filmes; }

    @Override
    public short size() {
        return TAMANHO_FIXO;
    }

    @Override
    public Lista clone() {
        return new Lista(this.id, this.userId, this.nome, this.isPadrao);
    }

    @Override
    public int compareTo(Lista outro) {
        return Integer.compare(this.id, outro.id);
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(TAMANHO_FIXO);
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.id);
        dos.writeInt(this.userId);
        dos.writeBoolean(this.isPadrao);

        byte[] nomeBytes = this.nome.getBytes(StandardCharsets.UTF_8);
        if (nomeBytes.length > TAMANHO_NOME) {
            throw new IOException("Nome da lista excede o tamanho máximo de " + TAMANHO_NOME + " bytes.");
        }
        dos.write(nomeBytes);
        for (int i = nomeBytes.length; i < TAMANHO_NOME; i++) {
            dos.writeByte(0);
        }

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.id = dis.readInt();
        this.userId = dis.readInt();
        this.isPadrao = dis.readBoolean();

        byte[] nomeBytes = new byte[TAMANHO_NOME];
        dis.read(nomeBytes);

        int len = 0;
        while(len < nomeBytes.length && nomeBytes[len] != 0) {
            len++;
        }
        this.nome = new String(nomeBytes, 0, len, StandardCharsets.UTF_8);

        this.filmes = new ArrayList<>();
    }
    
    @Override
    public String toString() {
        return "Lista [id=" + id + ", userId=" + userId + ", nome=" + nome + ", isPadrao=" + isPadrao + ", filmes na memória=" + (filmes != null ? filmes.size() : 0) + "]";
    }
}

