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
    private ArrayList<Filme> filmes;
    private int userId;      
    private boolean isPadrao;  

    public Lista() {
        this.id = -1;
        this.userId = -1;
        this.nome = "";
        this.isPadrao = false;
        this.filmes = new ArrayList<>();
    }

    public Lista(int id, int userId, String nome, boolean isPadrao, ArrayList<Filme> filmes) {
        this.id = id;
        this.userId = userId;
        this.nome = nome;
        this.isPadrao = isPadrao;
        this.filmes = filmes;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public ArrayList<Filme> getFilmes() { return filmes; }
    public void setFilmes(ArrayList<Filme> filmes) { this.filmes = filmes; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public boolean isPadrao() { return isPadrao; }
    public void setPadrao(boolean isPadrao) { this.isPadrao = isPadrao; }


    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeInt(this.userId);    
        dos.writeBoolean(this.isPadrao); 
        dos.writeUTF(this.nome);
        writeFilmeArray(this.filmes, dos);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.userId = dis.readInt();      
        this.isPadrao = dis.readBoolean();  
        this.nome = dis.readUTF();
        this.filmes = readFilmeArray(new ArrayList<>(), dis);
    }
    
    
    public void writeFilmeArray(ArrayList<Filme> list, DataOutputStream dos) throws IOException {
        dos.writeInt(list.size());
        for (Filme f : list) {
            byte[] bytes = f.toByteArray();
            dos.writeInt(bytes.length);
            dos.write(bytes);
        }
    }

    public ArrayList<Filme> readFilmeArray(ArrayList<Filme> list, DataInputStream dis) throws IOException {
        list.clear();
        int entityNum = dis.readInt();
        for (int i = 0; i < entityNum; i++) {
            int len = dis.readInt();
            byte[] bytes = new byte[len];
            dis.readFully(bytes);
            Filme f = new Filme(); 
            f.fromByteArray(bytes);
            list.add(f);
        }
        return list;
    }

    public String toString() {
        return "Lista [id=" + id + ", userId=" + userId + ", nome=" + nome + ", isPadrao=" + isPadrao + ", filmes=" + filmes.size() + "]";
    }
}