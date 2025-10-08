package com.example.tpaedsiii.repository.BD;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class User implements Registro{
    private int id;
    private String username;
    private String description;
    private String password;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    
    public User() {
        this(-1, "", "", "");
    }

    public User(int id, String username, String description, String password) {
        this.id = id;
        this.username = username;
        this.description = description;
        this.password = password;
    }

    public void fromByteArray(byte[] b) throws IOException{
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.username = dis.readUTF();
        this.description = dis.readUTF();
        this.password = dis.readUTF();
    }

     public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.username);
        dos.writeUTF(this.description);
        dos.writeUTF(this.password);
        return baos.toByteArray();
    }

    public String toString() {
        return "User[" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", description='" + description + '\'' +
                ", password='[PROTECTED]'" +
                ']';
    }
    
}
