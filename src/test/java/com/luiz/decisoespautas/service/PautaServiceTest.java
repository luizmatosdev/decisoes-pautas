package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.repositories.PautaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    Pauta pauta;

    @InjectMocks
    private PautaService pautaService;
    @Mock
    private PautaRepository pautaRepository;

    @BeforeEach
    void setUp() {
        pauta = new Pauta();
        pauta.setId(1L);
        pauta.setTitulo("Titulo");
        pauta.setDescricao("Descrição");
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        when(pautaRepository.findAll()).thenReturn(List.of(pauta));
        Pauta pautaTest = pautaService.findAll().getFirst();
        assertEquals(pauta.getId(), pautaTest.getId());
        assertEquals(pauta.getTitulo(), pautaTest.getTitulo());
        assertEquals(pauta.getDescricao(), pautaTest.getDescricao());
    }

    @Test
    void testFind() {
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));
        Pauta pautaTest = pautaService.find(pauta.getId());
        assertEquals(pauta.getId(), pautaTest.getId());
        assertEquals(pauta.getTitulo(), pautaTest.getTitulo());
        assertEquals(pauta.getDescricao(), pautaTest.getDescricao());
    }

    @Test
    void testFindNotFound() {
        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.empty());
        Long idPauta = pauta.getId();
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            pautaService.find(idPauta);
        });
        String erroEsperado = "Pauta não encontrada";
        String erro = exception.getMessage();
        assertEquals(erroEsperado, erro);
    }

    @Test
    void testSave() {
        when(pautaRepository.save(pauta)).thenReturn(pauta);
        Pauta pautaTest = pautaService.save(pauta);
        assertEquals(pauta.getId(), pautaTest.getId());
        assertEquals(pauta.getTitulo(), pautaTest.getTitulo());
        assertEquals(pauta.getDescricao(), pautaTest.getDescricao());
    }
}
