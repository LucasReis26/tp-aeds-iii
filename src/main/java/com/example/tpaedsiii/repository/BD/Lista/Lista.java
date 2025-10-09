package com.example.tpaedsiii.repository.BD.Lista;

import com.example.tpaedsiii.repository.BD.Filme.Filme;
import com.example.tpaedsiii.repository.BD.base.Registro;
import com.fasterxml.jackson.core.io.DataOutputAsStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.ArrayList;

public class Lista implements Registro {
    final int MAX_TAMANHO = 10_000_000;
    int id;
    String nome;
    ArrayList<Filme> filmes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public ArrayList<Filme> getFilmes() {
        return filmes;
    }

    public void setFilmes(ArrayList<Filme> filmes) {
        this.filmes = filmes;
    }

    public Lista(int id, String nome, ArrayList<Filme> filmes) {
        this.id = id;
        this.nome = nome;
        this.filmes = filmes;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        writeFilmeArray(this.filmes, dos);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {

        throw new UnsupportedOperationException("Unimplemented method 'fromByteArray'");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n"); 

        for (Filme f : filmes) {
            sb.append("  Filme {\n")
                    .append("    id=").append(f.getId()).append(",\n")
                    .append("    score=").append(f.getScore()).append(",\n")
                    .append("    title='").append(f.getTitle()).append("',\n")
                    .append("    releaseDate=").append(f.getReleaseDate()).append(",\n")
                    .append("    duration=").append(f.getDuration()).append(",\n")
                    .append("    directors=").append(f.getDirectors()).append(",\n")
                    .append("    actors=").append(f.getActors()).append(",\n")
                    .append("    rating=").append(f.getRating()).append("\n")
                    .append("  },\n");
        }

        if (!filmes.isEmpty()) {
            sb.setLength(sb.length() - 2);
        }

        sb.append("\n]");

        return sb.toString();
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
        int entityNum = dis.readInt(); // lê o tamanho da lista corretamente
        for (int i = 0; i < entityNum; i++) {
            int len = dis.readInt();
            if (len < 0 || len > MAX_TAMANHO)
                throw new IOException("Tamanho inválido: " + len);
            byte[] bytes = new byte[len];
            dis.readFully(bytes);
            Filme f = new Filme();
            f.fromByteArray(bytes);
            list.add(f);
        }
        return list;
    }

}
