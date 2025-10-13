package com.example.tpaedsiii.repository.dao.user;

import com.example.tpaedsiii.repository.bd.user.User;
import com.example.tpaedsiii.repository.bd.base.Arquivo;
import java.util.List; // Importado

public class UserDAO {
    private final Arquivo<User> arqUsers;

    public UserDAO() throws Exception {
        arqUsers = new Arquivo<>("data/Users.db", User.class.getConstructor());
    }

    public int incluirUser(User user) throws Exception {
        return arqUsers.create(user);
    }

    public User buscarUser(int id) throws Exception {
        return arqUsers.read(id);
    }
    
    /**
     * NOVO MÉTODO: Busca um usuário pelo seu nome de usuário (username).
     * AVISO: Esta operação é LENTA em bancos de dados grandes pois lê todos os registros.
     * @param username O nome de usuário a ser procurado.
     * @return O objeto User, ou null se não for encontrado.
     */
    public User buscarPorUsername(String username) throws Exception {
        List<User> todosOsUsuarios = arqUsers.readAll(); // Assume que readAll() existe em Arquivo.java
        for (User u : todosOsUsuarios) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    public boolean alterarUser(User user) throws Exception {
        return arqUsers.update(user);
    }

    public boolean excluirUser(int id) throws Exception {
        return arqUsers.delete(id);
    }
}