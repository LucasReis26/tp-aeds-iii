package com.example.tpaedsiii.repository.DAO;
import java.time.LocalDate;
import java.util.ArrayList;

import com.example.tpaedsiii.repository.BD.Arquivo;
import com.example.tpaedsiii.repository.BD.Filme;
public class FilmeDAO{
    private final Arquivo<Filme> arqFilmes;

    public static void main(String[] args) {
        try {
                   System.out.println("sim eu executei cara");
        ArrayList<String> diretores = new ArrayList<>();
        diretores.add("Steven Spielberg");
        diretores.add("George Lucas");

        // Lista de atores
        ArrayList<String> atores = new ArrayList<>();
        atores.add("Harrison Ford");
        atores.add("Mark Hamill");


           Filme filme = new Filme(
                95,                          // score
                "Star Wars: Uma Nova Esperança",  // title
                LocalDate.of(1977, 5, 25),   // releaseDate
                diretores,                    // directors
                atores,                       // actors
                10,                           // rating
                121                           // duration em minutos
        );

            FilmeDAO filmeDAO = new FilmeDAO();
            filmeDAO.incluirFilme(filme);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FilmeDAO() throws Exception{
        arqFilmes = new Arquivo<>("filmes", Filme.class.getConstructor());
    }

    public Filme buscarFilme(int id) throws Exception{
        return arqFilmes.read(id);
    }

    public boolean incluirFilme(Filme filme) throws Exception{
        return arqFilmes.create(filme) > 0;
    }

    public boolean alterarFilme(Filme filme) throws Exception{
        return arqFilmes.update(filme);
    }

    public boolean excluirFilme(int id) throws Exception{
        return arqFilmes.delete(id);
    }
    
}