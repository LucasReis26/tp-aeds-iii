package com.example.tpaedsiii.repository.BD;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

public class Arquivo<T extends Registro> {
    private static final int TAM_CABECALHO = 12; // 4 bytes (ID) + 8 bytes (lista deletados)
    private RandomAccessFile arquivo;
    private String nomeArquivo;
    private Constructor<T> construtor;

    public Arquivo(String nomeArquivo, Constructor<T> construtor) throws Exception {

        File diretorio = new File("./src/main/java/com/example/tpaedsiii/repository/BD/data/filmes");
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }

        this.nomeArquivo = diretorio.getPath() + "/" + nomeArquivo + ".db";

        this.construtor = construtor;
        this.arquivo = new RandomAccessFile(this.nomeArquivo, "rw");

        if (arquivo.length() == 0) {
            arquivo.writeInt(0);
            arquivo.writeLong(-1);
        }
    }

    public int create(T obj) throws Exception {
        arquivo.seek(0);
        int novoID = arquivo.readInt() + 1;
        arquivo.seek(0);
        arquivo.writeInt(novoID);
        obj.setId(novoID);
        byte[] dados = obj.toByteArray();
        long endereco = getDeleted(dados.length);
        if (endereco == -1) {
            arquivo.seek(arquivo.length());
            endereco = arquivo.getFilePointer();
            arquivo.writeByte(' '); // Lápide
            arquivo.writeShort(dados.length);
            arquivo.write(dados);
        } else {
            arquivo.seek(endereco);
            arquivo.writeByte(' '); // Remove a lápide
            arquivo.skipBytes(2);
            arquivo.write(dados);
        }
        return obj.getId();
    }

    public T read(int id) throws Exception {
        long posicaoAtual = TAM_CABECALHO;
        arquivo.seek(posicaoAtual);

        while (posicaoAtual < arquivo.length()) {
            if (arquivo.length() - posicaoAtual < 3) {
                System.err.println("Registro corrompido na posição " + posicaoAtual + ". A operação será interrompida.");
                return null;
            }
            byte lapide = arquivo.readByte();
            short tamanho = arquivo.readShort();

            if (tamanho < 0 || posicaoAtual + 3 + tamanho > arquivo.length()) {
                System.err.println("Registro corrompido na posição " + posicaoAtual + ". A operação será interrompida.");
                return null;
            }

            if (lapide == ' ') {
                byte[] dados = new byte[tamanho];
                arquivo.readFully(dados);
                T obj = construtor.newInstance();
                obj.fromByteArray(dados);
                if (obj.getId() == id) {
                    return obj;
                }
            } else {
                arquivo.skipBytes(tamanho);
            }
            // CORREÇÃO: Atualiza a posição para o próximo registro
            posicaoAtual += 3 + tamanho;
        }
        return null;
    }

    public boolean delete(int id) throws Exception {
        long posicaoAtual = TAM_CABECALHO;
        arquivo.seek(posicaoAtual);

        while (posicaoAtual < arquivo.length()) {
            if (arquivo.length() - posicaoAtual < 3) {
                System.err.println("Registro corrompido na posição " + posicaoAtual + ". A operação será interrompida.");
                return false;
            }
            byte lapide = arquivo.readByte();
            short tamanho = arquivo.readShort();

            if (tamanho < 0 || posicaoAtual + 3 + tamanho > arquivo.length()) {
                System.err.println("Registro corrompido na posição " + posicaoAtual + ". A operação será interrompida.");
                return false;
            }

            if (lapide == ' ') {
                byte[] dados = new byte[tamanho];
                arquivo.readFully(dados);
                T obj = construtor.newInstance();
                obj.fromByteArray(dados);
                if (obj.getId() == id) {
                    arquivo.seek(posicaoAtual);
                    arquivo.writeByte('*');
                    addDeleted(tamanho, posicaoAtual);
                    return true;
                }
            } else {
                arquivo.skipBytes(tamanho);
            }
            // CORREÇÃO: Atualiza a posição para o próximo registro
            posicaoAtual += 3 + tamanho;
        }
        return false;
    }

    public boolean update(T novoObj) throws Exception {
        long posicaoAtual = TAM_CABECALHO;
        arquivo.seek(posicaoAtual);

        while (posicaoAtual < arquivo.length()) {
            if (arquivo.length() - posicaoAtual < 3) {
                System.err.println("Registro corrompido na posição " + posicaoAtual + ". A operação será interrompida.");
                return false;
            }
            byte lapide = arquivo.readByte();
            short tamanho = arquivo.readShort();

            if (tamanho < 0 || posicaoAtual + 3 + tamanho > arquivo.length()) {
                System.err.println("Registro corrompido na posição " + posicaoAtual + ". A operação será interrompida.");
                return false;
            }

            if (lapide == ' ') {
                byte[] dados = new byte[tamanho];
                arquivo.readFully(dados);
                T obj = construtor.newInstance();
                obj.fromByteArray(dados);
                if (obj.getId() == novoObj.getId()) {
                    byte[] novosDados = novoObj.toByteArray();
                    short novoTam = (short) novosDados.length;

                    if (novoTam <= tamanho) {
                        arquivo.seek(posicaoAtual + 3);
                        arquivo.write(novosDados);
                    } else {
                        arquivo.seek(posicaoAtual);
                        arquivo.writeByte('*');
                        addDeleted(tamanho, posicaoAtual);

                        long novoEndereco = getDeleted(novosDados.length);
                        if (novoEndereco == -1) {
                            arquivo.seek(arquivo.length());
                            novoEndereco = arquivo.getFilePointer();
                            arquivo.writeByte(' ');
                            arquivo.writeShort(novoTam);
                            arquivo.write(novosDados);
                        } else {
                            arquivo.seek(novoEndereco);
                            arquivo.writeByte(' ');
                            arquivo.skipBytes(2);
                            arquivo.write(novosDados);
                        }
                    }
                    return true;
                }
            } else {
                arquivo.skipBytes(tamanho);
            }
            // CORREÇÃO: Atualiza a posição para o próximo registro
            posicaoAtual += 3 + tamanho;
        }
        return false;
    }

    private void addDeleted(int tamanhoEspaco, long enderecoEspaco) throws Exception {
        // Lógica de lista de deletados - Esta lógica pode precisar de revisão futura
        // mas não é a causa do bug atual.
        long posicao = 4;
        arquivo.seek(posicao);
        long endereco = arquivo.readLong();
        long proximo;

        if (endereco == -1) {
            arquivo.seek(4);
            arquivo.writeLong(enderecoEspaco);
            arquivo.seek(enderecoEspaco + 3);
            arquivo.writeLong(-1);
        } else {
            do {
                arquivo.seek(endereco + 1);
                int tamanho = arquivo.readShort();
                proximo = arquivo.readLong();

                if (tamanho > tamanhoEspaco) {
                    if (posicao == 4)
                        arquivo.seek(posicao);
                    else
                        arquivo.seek(posicao + 3);
                    arquivo.writeLong(enderecoEspaco);
                    arquivo.seek(enderecoEspaco + 3);
                    arquivo.writeLong(endereco);
                    break;
                }

                if (proximo == -1) {
                    arquivo.seek(endereco + 3);
                    arquivo.writeLong(enderecoEspaco);
                    arquivo.seek(enderecoEspaco + 3);
                    arquivo.writeLong(-1);
                    break;
                }

                posicao = endereco;
                endereco = proximo;
            } while (endereco != -1);
        }
    }

    private long getDeleted(int tamanhoNecessario) throws Exception {
        // Lógica de lista de deletados - Esta lógica pode precisar de revisão futura
        // mas não é a causa do bug atual.
        long posicao = 4;
        arquivo.seek(posicao);
        long endereco = arquivo.readLong();
        long proximo;
        int tamanho;

        while (endereco != -1) {
            arquivo.seek(endereco + 1);
            tamanho = arquivo.readShort();
            proximo = arquivo.readLong();

            if (tamanho > tamanhoNecessario) {
                if (posicao == 4)
                    arquivo.seek(posicao);
                else
                    arquivo.seek(posicao + 3);
                arquivo.writeLong(proximo);
                return endereco;
            }
            posicao = endereco;
            endereco = proximo;
        }
        return -1;
    }

    public void close() throws Exception {
        arquivo.close();
    }
}

