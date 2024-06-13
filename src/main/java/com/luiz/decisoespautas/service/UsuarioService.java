package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.entities.Usuario;
import com.luiz.decisoespautas.repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario find(String cpf) {
        return usuarioRepository.findById(cpf).orElseThrow(() -> new EntityNotFoundException("Usuário não cadastrado"));
    }

    public void criarUsuario(Usuario usuario) {
        validaCpf(usuario.getCpf());
        usuarioRepository.save(usuario);
    }

    private void validaCpf(String cpf) {
        if (cpf.length() != 11) {
            throw new IllegalArgumentException("CPF inválido");
        }
    }
}
