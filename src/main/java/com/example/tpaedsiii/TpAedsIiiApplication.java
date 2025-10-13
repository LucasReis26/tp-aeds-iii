package com.example.tpaedsiii;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.tpaedsiii.repository.dao.filme.FilmeDAO;

@SpringBootApplication
public class TpAedsIiiApplication {

	public static void main(String[] args) throws Exception {
        System.out.println("-> Iniciando o programa (versão sem Spring)...");

        FilmeDAO filmeDAO = new FilmeDAO();
        System.out.println("-> Objeto FilmeDAO criado.");

        // Add calls to your DAO methods here to test them
        
        System.out.println("-> Programa finalizado.");
	}

}
