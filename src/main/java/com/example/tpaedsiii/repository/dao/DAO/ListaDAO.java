package com.example.tpaedsiii.repository.dao;
import com.example.tpaedsiii.repository.bd.Lista.Lista;
import com.example.tpaedsiii.repository.bd.base.Arquivo;

public class ListaDAO {
    private final Arquivo<Lista> arqListas;

    public static void main(String[] args) {
        try {
            ListaDAO ListaDAO = new ListaDAO();
            Lista result = ListaDAO.buscarLista(2);
            System.out.println(result.toString());
            ListaDAO.excluirLista(3);
        } catch (Exception e) {
        }
    }

    public ListaDAO() throws Exception {
        arqListas = new Arquivo<>("Listas", Lista.class.getConstructor());
    }

    public int incluirLista(Lista Lista) throws Exception {
        return arqListas.create(Lista);
    }

    public Lista buscarLista(int id) throws Exception {
        return arqListas.read(id);
    }

    public boolean alterarLista(Lista Lista) throws Exception {
        return arqListas.update(Lista);
    }

    public boolean excluirLista(int id) throws Exception {
        return arqListas.delete(id);
    }

}

