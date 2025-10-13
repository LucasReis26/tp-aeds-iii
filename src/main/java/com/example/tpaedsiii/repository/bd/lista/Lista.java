package com.example.tpaedsiii.repository.bd.lista;

import com.example.tpaedsiii.repository.bd.base.Registro;
import com.example.tpaedsiii.repository.bd.filme.Filme;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Lista implements Registro {
    private int id;
    private String nome;
    private int userId;
    private boolean isPadrao;

    private ArrayList<Filme> filmes;

    public Lista() {
        this.id = -1;
        this.userId = -1;
        this.nome = "";
        this.isPadrao = false;
        this.filmes = new ArrayList<>();
    }

    public Lista(int id, int userId, String nome, boolean isPadrao) {
        this.id = id;
        this.userId = userId;
        this.nome = nome;
        this.isPadrao = isPadrao;
        this.filmes = new ArrayList<>();
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public boolean isPadrao() { return isPadrao; }
    public void setPadrao(boolean isPadrao) { this.isPadrao = isPadrao; }
    public ArrayList<Filme> getFilmes() { return filmes; }
    public void setFilmes(ArrayList<Filme> filmes) { this.filmes = filmes; }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeInt(this.userId);
        dos.writeBoolean(this.isPadrao);
        dos.writeUTF(this.nome);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.userId = dis.readInt();
        this.isPadrao = dis.readBoolean();
        this.nome = dis.readUTF();
        this.filmes = new ArrayList<>();
    }

    @Override
    public String toString() {
        // O toString pode continuar mostrando o tamanho da lista de filmes para debugging
        return "Lista [id=" + id + ", userId=" + userId + ", nome=" + nome + ", isPadrao=" + isPadrao + ", filmes na memória=" + filmes.size() + "]";
    }
}