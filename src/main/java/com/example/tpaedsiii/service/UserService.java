package com.example.tpaedsiii.service;

import com.example.tpaedsiii.model.user.User;
import com.example.tpaedsiii.repository.user.UserRepository;
import com.example.tpaedsiii.extra.RSAEncryptor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RSAEncryptor rsa;

    private static final int P = 61;
    private static final int Q = 53;
    private static final int E = 17;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.rsa = new RSAEncryptor(P, Q, E);
    }

    public int criarUsuario(User user) throws Exception {
        if (userRepository.buscarPorUsername(user.getUsername()) != null) {
            throw new Exception("Usuário já existe.");
        }

        String senhaOriginal = user.getPassword();

        String senhaCriptografada = rsa.encrypt(senhaOriginal);

        user.setPassword(senhaCriptografada);

        return userRepository.incluirUser(user);
    }

    public User buscarPorId(int id) throws Exception {
        User user = userRepository.buscarUser(id);

        if (user != null && user.getPassword() != null) {
            String senhaLimpa = rsa.decrypt(user.getPassword());
            user.setPassword(senhaLimpa);
        }
        return user;
    }

    public List<User> listarTodos() throws Exception {
        List<User> users = userRepository.listarTodos();
        for (User u : users) {
            if (u.getPassword() != null) {
                u.setPassword(rsa.decrypt(u.getPassword()));
            }
        }
        return users;
    }

    public User alterarUsuario(int id, String novoUsername, String novaDescricao) throws Exception {
        User user = userRepository.buscarUser(id);

        if (user == null)
            throw new Exception("Usuário não encontrado.");

        String senhaCifrada = user.getPassword();

        user.setUsername(novoUsername);
        user.setDescription(novaDescricao);

        user.setPassword(senhaCifrada);

        userRepository.alterarUser(user);

        user.setPassword(rsa.decrypt(senhaCifrada));

        return user;
    }

    public boolean deletarUsuario(int id) throws Exception {
        return userRepository.excluirUser(id);
    }
}