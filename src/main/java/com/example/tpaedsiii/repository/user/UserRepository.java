package com.example.tpaedsiii.repository.user;

import com.example.tpaedsiii.model.user.User;
import com.example.tpaedsiii.repository.bd.indexes.base.HashExtensivel;

import jakarta.annotation.PostConstruct;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private HashExtensivel<User> hashUsers;

    public UserRepository(){}

    @PostConstruct
    public void init() throws Exception {
        hashUsers = new HashExtensivel<>(User.class.getConstructor(), 4, "data/Users_d.db", "data/Users_c.db");
    }

    public int incluirUser(User user) throws Exception {
        return hashUsers.create(user);
    }

    public User buscarUser(int id) throws Exception {
        return hashUsers.read(id);
    }

    public List<User> listarTodos() throws Exception {
        return hashUsers.readAll();
    }
    
    public User buscarPorUsername(String username) throws Exception {
        List<User> todosOsUsuarios = hashUsers.readAll();
        for (User u : todosOsUsuarios) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    public boolean alterarUser(User user) throws Exception {
        return hashUsers.update(user);
    }

    public boolean excluirUser(int id) throws Exception {
        return hashUsers.delete(id);
    }
}