package com.example.tpaedsiii.repository.dao.user;

import com.example.tpaedsiii.repository.bd.indexes.base.HashExtensivel;
import com.example.tpaedsiii.repository.bd.user.User;
import java.util.List;

public class UserDAO {
    private final HashExtensivel<User> hashUsers;

    public UserDAO() throws Exception {
        hashUsers = new HashExtensivel<>(User.class.getConstructor(), 4, "data/Users_d.db", "data/Users_c.db");
    }

    public int incluirUser(User user) throws Exception {
        return hashUsers.create(user);
    }

    public User buscarUser(int id) throws Exception {
        return hashUsers.read(id);
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