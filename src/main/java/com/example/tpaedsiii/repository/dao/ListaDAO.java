package com.example.tpaedsiii.repository.dao;
import com.example.tpaedsiii.repository.bd.lista.Lista;

import java.util.ArrayList;

import com.example.tpaedsiii.repository.bd.base.Arquivo;
import com.example.tpaedsiii.repository.bd.filme.Filme;

public class ListaDAO {
    private final Arquivo<Lista> arqListas;


    public ListaDAO() throws Exception {
        arqListas = new Arquivo<>("Listas", Lista.class.getConstructor());
        ArrayList<Filme> filmesFavoritos = new ArrayList<>();
            filmesFavoritos.add(new Filme(1, "O Poderoso Chefão", new Date(), 9.2f));
            filmesFavoritos.add(new Filme(2, "O Senhor dos Anéis: O Retorno do Rei", new Date(), 9.0f));

            Lista listaFavoritos = new Lista(0, "Meus Favoritos", filmesFavoritos);

            // --- 1. TESTE DE INCLUSÃO (CREATE) ---
            System.out.println("\n--- INCLUINDO LISTA ---");
            int idFavoritos = dao.incluirLista(listaFavoritos);
            System.out.println("Lista 'Meus Favoritos' criada com ID: " + idFavoritos);

            // --- 2. TESTE DE BUSCA (READ) ---
            System.out.println("\n--- BUSCANDO LISTA ---");
            Lista listaBuscada = dao.buscarLista(idFavoritos);
            System.out.println("Busca pelo ID " + idFavoritos + ":\n" + listaBuscada);

            // --- 3. TESTE DE ALTERAÇÃO (UPDATE) ---
            System.out.println("\n--- ALTERANDO A LISTA (ADICIONANDO UM NOVO FILME) ---");
            
            // Lógica para adicionar um novo filme
            Filme novoFilme = new Filme(3, "A Origem", new Date(), 8.8f);
            listaBuscada.getFilmes().add(novoFilme); // Modifica o objeto em memória
            
            boolean alterou = dao.alterarLista(listaBuscada); // Persiste a alteração no arquivo
            
            if (alterou) {
                System.out.println("Alteração bem-sucedida! Buscando novamente para confirmar:");
                Lista listaAlterada = dao.buscarLista(idFavoritos);
                System.out.println(listaAlterada);
            } else {
                System.out.println("Falha na alteração.");
            }

            // --- 4. TESTE DE EXCLUSÃO (DELETE) ---
            System.out.println("\n--- EXCLUINDO A LISTA ---");
            boolean excluiu = dao.excluirLista(idFavoritos);
            if (excluiu) {
                System.out.println("Exclusão bem-sucedida! Tentando buscar novamente...");
                Lista listaExcluida = dao.buscarLista(idFavoritos);
                System.out.println("Resultado da busca: " + (listaExcluida == null ? "NÃO ENCONTRADA (CORRETO)" : "ERRO!"));
            }

        } catch (Exception e) {
            System.err.println("Ocorreu um erro fatal durante o teste:");
            e.printStackTrace();
        }
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

