package com.example.tpaedsiii.repository.dao.lista;
import com.example.tpaedsiii.repository.bd.lista.Lista;
import com.example.tpaedsiii.repository.bd.base.Arquivo;

public class ListaDAO {
    private final Arquivo<Lista> arqListas;


    public ListaDAO() throws Exception {
        arqListas = new Arquivo<>("Listas", Lista.class.getConstructor());
        incluirLista(null);
        buscarLista(0);
        alterarLista(null);

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

