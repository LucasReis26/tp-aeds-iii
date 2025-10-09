package com.example.tpaedsiii.repository.bd.review;

import com.example.tpaedsiii.repository.bd.base.Registro;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Representa a relação N-N entre um Usuário e um Filme, armazenando a avaliação.
 * Cada instância desta classe é um registro único de avaliação.
 */
public class Review implements Registro {

    private int id;        
    private int userId;     
    private int filmeId;   
    private float nota;      
    private String comentario; 
    private Date data;       

    public Review() {
        this.id = -1;
        this.userId = -1;
        this.filmeId = -1;
        this.nota = 0.0f;
        this.comentario = "";
        this.data = new Date();
    }

    /**
     * Construtor completo para criar uma nova avaliação.
     */
    public Review(int id, int userId, int filmeId, float nota, String comentario, Date data) {
        this.id = id;
        this.userId = userId;
        this.filmeId = filmeId;
        this.nota = nota;
        this.comentario = comentario;
        this.data = data;
    }

    public int getId() { return this.id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getFilmeId() { return filmeId; }
    public void setFilmeId(int filmeId) { this.filmeId = filmeId; }
    public float getNota() { return nota; }
    public void setNota(float nota) { this.nota = nota; }



    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeInt(this.userId);
        dos.writeInt(this.filmeId);
        dos.writeFloat(this.nota);
        dos.writeUTF(this.comentario);
        dos.writeLong(this.data.getTime());
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.userId = dis.readInt();
        this.filmeId = dis.readInt();
        this.nota = dis.readFloat();
        this.comentario = dis.readUTF();
        this.data = new Date(dis.readLong());
    }
    
    public String toString() {
        return "Avaliacao [id=" + id + ", userId=" + userId + ", filmeId=" + filmeId + 
               ", nota=" + nota + ", comentario='" + comentario + "', data=" + data + "]";
    }
}