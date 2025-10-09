package com.example.tpaedsiii.repository.dao.review;

import com.example.tpaedsiii.repository.bd.review.Review;
import com.example.tpaedsiii.repository.bd.base.Arquivo;
import java.util.ArrayList;

public class ReviewDAO {

    private final Arquivo<Review> arqAvaliacoes;

    public ReviewDAO() throws Exception {
        arqAvaliacoes = new Arquivo<>("Avaliacoes", Review.class.getConstructor());
    }

    public int create(Review Review) throws Exception {
        return arqAvaliacoes.create(Review);
    }

    public Review read(int id) throws Exception {
        return arqAvaliacoes.read(id);
    }
    
    public boolean update(Review Review) throws Exception {
        return arqAvaliacoes.update(Review);
    }

    public boolean delete(int id) throws Exception {
        return arqAvaliacoes.delete(id);
    }
    /*Todos os metodos abaixo precisam de indice
     TODO: public ArrayList<Review> buscarAvaliacoesPorUsuario(int userId) throws Exception {
 
     }

     TODO: public ArrayList<Review> buscarAvaliacoesPorFilme(int filmeId) throws Exception {
    
     }

    TODO: public Review buscarReviewUnica(int userId, int filmeId) throws Exception {
    
    */ 
}