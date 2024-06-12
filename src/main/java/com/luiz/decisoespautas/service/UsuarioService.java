package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.entities.Usuario;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    public Usuario criarUsuario(String cpf) {
        validaCpf(cpf);
        Usuario usuario = new Usuario();
        usuario.setCpf(cpf);
        return usuario;
    }

    private void validaCpf(String cpf) {
        if (cpf.length() != 11) {
            throw new IllegalArgumentException("CPF inválido");
        }
    }
}
