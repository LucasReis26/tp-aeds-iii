package com.example.tpaedsiii.model.filme;
import com.example.tpaedsiii.repository.bd.base.Registro;
import com.example.tpaedsiii.repository.bd.indexes.base.RegistroHash;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.ArrayList;

public class Filme implements Registro, RegistroHash<Filme> {
    final int MAX_TAMANHO = 10_000_000;
    private static final int TAMANHO_FIXO_HASH = 256;
    private int id;
    private int score;
    private String title;
    private LocalDate releaseDate;
    private long duration;
    ArrayList<String> directors = new ArrayList<String>();
    ArrayList<String> actors = new ArrayList<String>();
    private int rating;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ArrayList<String> getDirectors() {
        return directors;
    }

    public void setDirectors(ArrayList<String> directors) {
        this.directors = directors;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public void setActors(ArrayList<String> actors) {
        this.actors = actors;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Filme() {
        this.id = -1;  
        this.directors = new ArrayList<>();
        this.actors = new ArrayList<>();
    }

    public Filme(int score, String title, LocalDate releaseDate, ArrayList<String> directors, ArrayList<String> actors,
            int rating, long duration) {
        this.id = -1; 
        this.score = score;
        this.title = title;
        this.releaseDate = releaseDate;
        this.directors = directors;
        this.actors = actors;
        this.rating = rating;
        this.duration = duration;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeInt(this.score);
        dos.writeUTF(this.title);
        dos.writeLong(this.releaseDate.toEpochDay());
        dos.writeLong(this.duration);
        writeStringArray(directors, dos);
        writeStringArray(actors, dos);
        dos.writeInt(rating);
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.score = dis.readInt();
        this.title = dis.readUTF();
        this.releaseDate = LocalDate.ofEpochDay(dis.readLong());
        this.duration = dis.readLong();
        this.directors = readStringArray(directors, dis);
        this.actors = readStringArray(actors, dis);
        this.rating = dis.readInt();

    }

    public void writeStringArray(ArrayList<String> list, DataOutputStream dos) throws IOException {
    dos.writeInt(list.size()); // escreve o número de elementos da lista
    for (String s : list) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        dos.writeInt(bytes.length);
        dos.write(bytes);
    }
}

public ArrayList<String> readStringArray(ArrayList<String> list, DataInputStream dis) throws IOException {
    list.clear();
    int entityNum = dis.readInt(); // lê o tamanho da lista corretamente
    for (int i = 0; i < entityNum; i++) {
        int len = dis.readInt();
        if (len < 0 || len > MAX_TAMANHO)
            throw new IOException("Tamanho inválido: " + len);
        byte[] bytes = new byte[len];
        dis.readFully(bytes);
        list.add(new String(bytes, StandardCharsets.UTF_8));
    }
    return list;
}


    public String toString() {
        return "Filme {" +
                "id=" + id +
                ", score=" + score +
                ", title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                ", directors=" + directors +
                ", actors=" + actors +
                ", rating=" + rating +
                '}';
    }

    public int hashCode() {
        return this.id;
    }

    public int size() {
        return TAMANHO_FIXO_HASH;
    }

}
