package com.example.tpaedsiii.model.user;

import com.example.tpaedsiii.repository.bd.base.Registro;
import com.example.tpaedsiii.repository.bd.indexes.base.RegistroHash; // Importado
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class User implements Registro, RegistroHash<User> {
    private int id;
    private String username;
    private String description;
    private String password;

    private static final int TAMANHO_FIXO_HASH = 256;

    public User() { this(-1, "", "", ""); }
    public User(int id, String username, String description, String password) {
        this.id = id; this.username = username; this.description = description; this.password = password;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    @Override public int hashCode() { return this.id; }
    @Override public short size() { return TAMANHO_FIXO_HASH; }

    @Override
    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id);
        dos.writeUTF(this.username);
        dos.writeUTF(this.description);
        dos.writeUTF(this.password);

        byte[] dadosVariaveis = baos.toByteArray();
        if (dadosVariaveis.length > TAMANHO_FIXO_HASH) {
            throw new IOException("Tamanho do usuário excede o buffer fixo de " + TAMANHO_FIXO_HASH + " bytes.");
        }
        byte[] bufferFixo = new byte[TAMANHO_FIXO_HASH];
        System.arraycopy(dadosVariaveis, 0, bufferFixo, 0, dadosVariaveis.length);
        return bufferFixo;
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException{
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        this.id = dis.readInt();
        this.username = dis.readUTF();
        this.description = dis.readUTF();
        this.password = dis.readUTF();
    }

    @Override
    public String toString() {
        return "User[id=" + id + ", username='" + username + "', description='" + description + "', password='[PROTECTED]']";
    }
}