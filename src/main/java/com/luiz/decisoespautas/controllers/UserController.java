package com.luiz.decisoespautas.controllers;

import com.luiz.decisoespautas.entities.Usuario;
import com.luiz.decisoespautas.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping(value = "/usuario", consumes = "application/json")
    public Usuario criarUsuario(@RequestBody String cpf) {
        return usuarioService.criarUsuario(cpf);
    }

}
