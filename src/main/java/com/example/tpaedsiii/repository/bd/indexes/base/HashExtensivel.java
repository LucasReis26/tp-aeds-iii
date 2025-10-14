package com.example.tpaedsiii.repository.bd.indexes.base;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class HashExtensivel<T extends RegistroHash<T>> {

    String nomeArquivoDiretorio;
    String nomeArquivoCestos;
    RandomAccessFile arqDiretorio;
    RandomAccessFile arqCestos;
    int quantidadeDadosPorCesto;
    Diretorio diretorio;
    Constructor<T> construtor;
    private static final int TAMANHO_CABECALHO_ID = 4; // 4 bytes para o contador de ID (int)

    // As classes internas Cesto e Diretorio permanecem inalteradas
    public class Cesto {
        Constructor<T> construtor;
        short quantidadeMaxima;
        int bytesPorElemento;
        short bytesPorCesto;
        byte profundidadeLocal;
        short quantidade;
        ArrayList<T> elementos;

        public Cesto(Constructor<T> ct, int qtdmax) throws Exception { this(ct, qtdmax, 0); }
        public Cesto(Constructor<T> ct, int qtdmax, int pl) throws Exception {
            construtor = ct;
            if (qtdmax > 32767) throw new Exception("Quantidade máxima de 32.767 elementos");
            if (pl > 127) throw new Exception("Profundidade local máxima de 127 bits");
            profundidadeLocal = (byte) pl;
            quantidade = 0;
            quantidadeMaxima = (short) qtdmax;
            elementos = new ArrayList<>(quantidadeMaxima);
            if (ct.getGenericParameterTypes().length == 0) { // Garante que o construtor é vazio
                 bytesPorElemento = ct.newInstance().size();
            } else {
                 throw new InstantiationException("Construtor da classe genérica não é vazio.");
            }
            bytesPorCesto = (short) (bytesPorElemento * quantidadeMaxima + 3);
        }
        public byte[] toByteArray() throws Exception {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeByte(profundidadeLocal);
            dos.writeShort(quantidade);
            for (int i = 0; i < quantidade; i++) {
                dos.write(elementos.get(i).toByteArray());
            }
            byte[] vazio = new byte[bytesPorElemento];
            for (int i = quantidade; i < quantidadeMaxima; i++) {
                dos.write(vazio);
            }
            return baos.toByteArray();
        }
        public void fromByteArray(byte[] ba) throws Exception {
            ByteArrayInputStream bais = new ByteArrayInputStream(ba);
            DataInputStream dis = new DataInputStream(bais);
            profundidadeLocal = dis.readByte();
            quantidade = dis.readShort();
            elementos = new ArrayList<>(quantidadeMaxima);
            byte[] dados = new byte[bytesPorElemento];
            for (int i = 0; i < quantidadeMaxima; i++) {
                dis.read(dados);
                T elem = construtor.newInstance();
                elem.fromByteArray(dados);
                elementos.add(elem);
            }
        }
        public boolean create(T elem) {
            if (full()) return false;
            int i = quantidade - 1;
            while (i >= 0 && elem.hashCode() < elementos.get(i).hashCode()) i--;
            elementos.add(i + 1, elem);
            quantidade++;
            return true;
        }
        public T read(int chave) {
            if (empty()) return null;
            int i = 0;
            while (i < quantidade && chave > elementos.get(i).hashCode()) i++;
            if (i < quantidade && chave == elementos.get(i).hashCode()) return elementos.get(i);
            return null;
        }
        public boolean update(T elem) {
            if (empty()) return false;
            int i = 0;
            while (i < quantidade && elem.hashCode() > elementos.get(i).hashCode()) i++;
            if (i < quantidade && elem.hashCode() == elementos.get(i).hashCode()) {
                elementos.set(i, elem);
                return true;
            }
            return false;
        }
        public boolean delete(int chave) {
            if (empty()) return false;
            int i = 0;
            while (i < quantidade && chave > elementos.get(i).hashCode()) i++;
            if (i < quantidade && chave == elementos.get(i).hashCode()) {
                elementos.remove(i);
                quantidade--;
                return true;
            }
            return false;
        }
        public boolean empty() { return quantidade == 0; }
        public boolean full() { return quantidade == quantidadeMaxima; }
        public String toString() {
            String s = "Profundidade Local: " + profundidadeLocal + "\nQuantidade: " + quantidade + "\n| ";
            for (int i = 0; i < quantidade; i++) s += elementos.get(i).toString() + " | ";
            for (int i = quantidade; i < quantidadeMaxima; i++) s += "- | ";
            return s;
        }
        public int size() { return bytesPorCesto; }
    }
    protected class Diretorio {
        byte profundidadeGlobal;
        long[] enderecos;
        public Diretorio() { profundidadeGlobal = 0; enderecos = new long[1]; enderecos[0] = 0; }
        public boolean atualizaEndereco(int p, long e) { if (p >= Math.pow(2, profundidadeGlobal)) return false; enderecos[p] = e; return true; }
        public byte[] toByteArray() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeByte(profundidadeGlobal);
            int quantidade = (int) Math.pow(2, profundidadeGlobal);
            for (int i = 0; i < quantidade; i++) dos.writeLong(enderecos[i]);
            return baos.toByteArray();
        }
        public void fromByteArray(byte[] ba) throws IOException {
            ByteArrayInputStream bais = new ByteArrayInputStream(ba);
            DataInputStream dis = new DataInputStream(bais);
            profundidadeGlobal = dis.readByte();
            int quantidade = (int) Math.pow(2, profundidadeGlobal);
            enderecos = new long[quantidade];
            for (int i = 0; i < quantidade; i++) enderecos[i] = dis.readLong();
        }
        public String toString() {
            String s = "\nProfundidade global: " + profundidadeGlobal;
            int quantidade = (int) Math.pow(2, profundidadeGlobal);
            for (int i = 0; i < quantidade; i++) s += "\n" + i + ": " + enderecos[i];
            return s;
        }
        protected long endereço(int p) { if (p >= Math.pow(2, profundidadeGlobal)) return -1; return enderecos[p]; }
        protected boolean duplica() {
            if (profundidadeGlobal == 127) return false;
            profundidadeGlobal++;
            int q1 = (int) Math.pow(2, profundidadeGlobal - 1);
            int q2 = (int) Math.pow(2, profundidadeGlobal);
            long[] novosEnderecos = new long[q2];
            for (int i = 0; i < q1; i++) novosEnderecos[i] = enderecos[i];
            for (int i = q1; i < q2; i++) novosEnderecos[i] = enderecos[i - q1];
            enderecos = novosEnderecos;
            return true;
        }
        protected int hash(int chave) { return Math.abs(chave) % (int) Math.pow(2, profundidadeGlobal); }
        protected int hash2(int chave, int pl) { return Math.abs(chave) % (int) Math.pow(2, pl); }
    }

    public HashExtensivel(Constructor<T> ct, int n, String nd, String nc) throws Exception {
        construtor = ct;
        quantidadeDadosPorCesto = n;
        nomeArquivoDiretorio = nd;
        nomeArquivoCestos = nc;

        File fileDiretorio = new File(nd);
        if (fileDiretorio.getParentFile() != null) fileDiretorio.getParentFile().mkdirs();
        File fileCestos = new File(nc);
        if (fileCestos.getParentFile() != null) fileCestos.getParentFile().mkdirs();

        arqDiretorio = new RandomAccessFile(nomeArquivoDiretorio, "rw");
        arqCestos = new RandomAccessFile(nomeArquivoCestos, "rw");

        if (arqDiretorio.length() == 0) {
            arqDiretorio.writeInt(0);
            diretorio = new Diretorio();
            byte[] bd = diretorio.toByteArray();
            arqDiretorio.write(bd);
            if (arqCestos.length() == 0) {
                Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
                bd = c.toByteArray();
                arqCestos.seek(0);
                arqCestos.write(bd);
            }
        }
    }

    public synchronized int create(T elem) throws Exception {
        arqDiretorio.seek(0);
        int ultimoID = arqDiretorio.readInt();
        int novoID = ultimoID + 1;
        elem.setId(novoID);
        arqDiretorio.seek(0);
        arqDiretorio.writeInt(novoID);

        byte[] bd = new byte[(int) (arqDiretorio.length() - TAMANHO_CABECALHO_ID)];
        arqDiretorio.seek(TAMANHO_CABECALHO_ID);
        arqDiretorio.read(bd);
        diretorio = new Diretorio();
        diretorio.fromByteArray(bd);

        int i = diretorio.hash(elem.hashCode());
        long enderecoCesto = diretorio.endereço(i);
        Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(enderecoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);

        if (!c.full()) {
            c.create(elem);
            arqCestos.seek(enderecoCesto);
            arqCestos.write(c.toByteArray());
            return novoID;
        }

        byte pl = c.profundidadeLocal;
        if (pl >= diretorio.profundidadeGlobal) diretorio.duplica();
        byte pg = diretorio.profundidadeGlobal;

        Cesto c1 = new Cesto(construtor, quantidadeDadosPorCesto, pl + 1);
        arqCestos.seek(enderecoCesto);
        arqCestos.write(c1.toByteArray());

        Cesto c2 = new Cesto(construtor, quantidadeDadosPorCesto, pl + 1);
        long novoEndereco = arqCestos.length();
        arqCestos.seek(novoEndereco);
        arqCestos.write(c2.toByteArray());

        int inicio = diretorio.hash2(elem.hashCode(), c.profundidadeLocal);
        int deslocamento = (int) Math.pow(2, pl);
        int max = (int) Math.pow(2, pg);
        boolean troca = false;
        for (int j = inicio; j < max; j += deslocamento) {
            if (troca) diretorio.atualizaEndereco(j, novoEndereco);
            troca = !troca;
        }

        bd = diretorio.toByteArray();
        arqDiretorio.seek(TAMANHO_CABECALHO_ID);
        arqDiretorio.write(bd);
        arqDiretorio.setLength(TAMANHO_CABECALHO_ID + bd.length);

        for (int j = 0; j < c.quantidade; j++) this.create(c.elementos.get(j));
        this.create(elem);

        return novoID;
    }
    
    public T read(int chave) throws Exception {
        byte[] bd = new byte[(int) (arqDiretorio.length() - TAMANHO_CABECALHO_ID)];
        arqDiretorio.seek(TAMANHO_CABECALHO_ID);
        arqDiretorio.read(bd);
        diretorio = new Diretorio();
        diretorio.fromByteArray(bd);
        int i = diretorio.hash(chave);
        long enderecoCesto = diretorio.endereço(i);
        if (enderecoCesto == -1) return null;
        Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(enderecoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);
        return c.read(chave);
    }
    
    public List<T> readAll(int chave) throws Exception {
        byte[] bd = new byte[(int) (arqDiretorio.length() - TAMANHO_CABECALHO_ID)];
        arqDiretorio.seek(TAMANHO_CABECALHO_ID);
        arqDiretorio.read(bd);
        diretorio = new Diretorio();
        diretorio.fromByteArray(bd);
        int i = diretorio.hash(chave);
        long enderecoCesto = diretorio.endereço(i);
        if (enderecoCesto == -1) return new ArrayList<>();
        Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(enderecoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);
        ArrayList<T> resultados = new ArrayList<>();
        for (int j = 0; j < c.quantidade; j++) {
            T elem = c.elementos.get(j);
            if (elem.hashCode() == chave) resultados.add(elem);
        }
        return resultados;
    }
    
    public List<T> readAll() throws Exception {
        ArrayList<T> todosOsElementos = new ArrayList<>();
        arqCestos.seek(0);
        while (arqCestos.getFilePointer() < arqCestos.length()) {
            Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
            byte[] ba = new byte[c.size()];
            arqCestos.read(ba);
            c.fromByteArray(ba);
            for (int i = 0; i < c.quantidade; i++) todosOsElementos.add(c.elementos.get(i));
        }
        return todosOsElementos;
    }

    public synchronized boolean update(T elem) throws Exception {
        byte[] bd = new byte[(int) (arqDiretorio.length() - TAMANHO_CABECALHO_ID)];
        arqDiretorio.seek(TAMANHO_CABECALHO_ID);
        arqDiretorio.read(bd);
        diretorio = new Diretorio();
        diretorio.fromByteArray(bd);
        int i = diretorio.hash(elem.hashCode());
        long enderecoCesto = diretorio.endereço(i);
        if (enderecoCesto == -1) return false;
        Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(enderecoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);
        if (!c.update(elem)) return false;
        arqCestos.seek(enderecoCesto);
        arqCestos.write(c.toByteArray());
        return true;
    }
    
    public synchronized boolean delete(int chave) throws Exception {
        byte[] bd = new byte[(int) (arqDiretorio.length() - TAMANHO_CABECALHO_ID)];
        arqDiretorio.seek(TAMANHO_CABECALHO_ID);
        arqDiretorio.read(bd);
        diretorio = new Diretorio();
        diretorio.fromByteArray(bd);
        int i = diretorio.hash(chave);
        long enderecoCesto = diretorio.endereço(i);
        if (enderecoCesto == -1) return false;
        Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(enderecoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);
        if (!c.delete(chave)) return false;
        arqCestos.seek(enderecoCesto);
        arqCestos.write(c.toByteArray());
        return true;
    }
    
    public synchronized boolean delete(T elem) throws Exception {
        byte[] bd = new byte[(int) (arqDiretorio.length() - TAMANHO_CABECALHO_ID)];
        arqDiretorio.seek(TAMANHO_CABECALHO_ID);
        arqDiretorio.read(bd);
        diretorio = new Diretorio();
        diretorio.fromByteArray(bd);
        int i = diretorio.hash(elem.hashCode());
        long enderecoCesto = diretorio.endereço(i);
        if (enderecoCesto == -1) return false;
        Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(enderecoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);
        boolean deleted = false;
        for (int j = 0; j < c.quantidade; j++) {
            if (c.elementos.get(j).equals(elem)) {
                c.elementos.remove(j);
                c.quantidade--;
                deleted = true;
                break;
            }
        }
        if (!deleted) return false;
        arqCestos.seek(enderecoCesto);
        arqCestos.write(c.toByteArray());
        return true;
    }

    public void print() {
        try {
            byte[] bd = new byte[(int) (arqDiretorio.length() - TAMANHO_CABECALHO_ID)];
            arqDiretorio.seek(TAMANHO_CABECALHO_ID);
            arqDiretorio.read(bd);
            diretorio = new Diretorio();
            diretorio.fromByteArray(bd);
            System.out.println("\nDIRETÓRIO ------------------");
            System.out.println(diretorio);
            System.out.println("\nCESTOS ---------------------");
            arqCestos.seek(0);
            while (arqCestos.getFilePointer() < arqCestos.length()) {
                System.out.println("Endereço: " + arqCestos.getFilePointer());
                Cesto c = new Cesto(construtor, quantidadeDadosPorCesto);
                byte[] ba = new byte[c.size()];
                arqCestos.read(ba);
                c.fromByteArray(ba);
                System.out.println(c + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}