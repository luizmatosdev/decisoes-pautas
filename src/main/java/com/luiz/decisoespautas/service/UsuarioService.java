package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.entities.Usuario;
import com.luiz.decisoespautas.repositories.UsuarioRepository;
import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario criarUsuario(Usuario usuario) {
        validaCpf(usuario.getCpf());
        Optional<Usuario> existeusuario = usuarioRepository.findById(usuario.getCpf());
        if (existeusuario.isPresent()) {
            throw new DuplicateRequestException("CPF já cadastrado");
        }
        usuarioRepository.save(usuario);
        return usuario;
    }

    private void validaCpf(String cpf) {
        if (cpf.length() != 11) {
            throw new IllegalArgumentException("CPF inválido");
        }
    }
}
