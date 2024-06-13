package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.entities.Usuario;
import com.luiz.decisoespautas.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    Usuario usuario;

    @InjectMocks
    private UsuarioService usuarioService;
    @Mock
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setCpf("01234567890");
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEncontraUsuario() {
        when(usuarioRepository.findById("01234567890")).thenReturn(Optional.of(usuario));
        Usuario usuarioFind = usuarioService.find("01234567890");
        assertEquals(usuario.getCpf(), usuarioFind.getCpf());
        assertNotNull(usuarioFind);
    }

    @Test
    void testCriarUsuario() {
        try {
            usuarioService.criarUsuario(usuario);
        } catch(Exception e) {
            fail("Não deve conter erros");
        }
    }

    @Test
    void testErroCriarUsuarioCpfInvalido() {
        Usuario usuarioInvalido = new Usuario();
        usuarioInvalido.setCpf("012345678900");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.criarUsuario(usuarioInvalido);
        });

        String erroEsperado = "CPF inválido";
        String erro = exception.getMessage();
        assertEquals(erroEsperado, erro);
    }
}
