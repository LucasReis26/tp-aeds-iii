package com.example.tpaedsiii.service;

import com.example.tpaedsiii.model.user.User;
import com.example.tpaedsiii.repository.user.UserRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User buscarPorId(int id) throws Exception {
        return userRepository.buscarUser(id);
    }
    
    public List<User> listarTodosUsuarios() throws Exception {
        return userRepository.listarTodos();
    }

    public int criarUsuario(User user) throws Exception {
        // Regra de Negócio: Não permitir usernames duplicados.
        if (userRepository.buscarPorUsername(user.getUsername()) != null) {
            throw new Exception("Nome de usuário '" + user.getUsername() + "' já existe.");
        }
        return userRepository.incluirUser(user);
    }
    
    public boolean deletarUsuario(int id) throws Exception {
        // Lógica de Negócio Futura: Antes de deletar, deletar todas as reviews e listas do usuário.
        return userRepository.excluirUser(id);
    }

    public User alterarUsuario(int id, String novoUsername, String novaDescricao) throws Exception {
        User user = userRepository.buscarUser(id);
        if (user == null) {
            throw new Exception("Usuário com ID " + id + " não encontrado para alteração.");
        }
        user.setUsername(novoUsername);
        user.setDescription(novaDescricao);
        userRepository.alterarUser(user);
        return user;
    }
}