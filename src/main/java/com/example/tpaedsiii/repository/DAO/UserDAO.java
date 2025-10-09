package com.example.tpaedsiii.repository.DAO;
import com.example.tpaedsiii.repository.BD.User.User;
import com.example.tpaedsiii.repository.BD.base.Arquivo;

public class UserDAO {
    private final Arquivo<User> arqUsers;

    public UserDAO() throws Exception {
        arqUsers = new Arquivo<>("Users", User.class.getConstructor());
    }

    public int incluirUser(User User) throws Exception {
        return arqUsers.create(User);
    }

    public User buscarUser(int id) throws Exception {
        return arqUsers.read(id);
    }

    public boolean alterarUser(User User) throws Exception {
        return arqUsers.update(User);
    }

    public boolean excluirUser(int id) throws Exception {
        return arqUsers.delete(id);
    }

}

