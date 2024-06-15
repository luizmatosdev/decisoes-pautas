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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    Pauta pauta;
    Pauta pautaCancelada;

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

        pautaCancelada = new Pauta();
        pautaCancelada.setId(2L);
        pautaCancelada.setTitulo("Titulo Cancelada");
        pautaCancelada.setDescricao("Descrição Cancelada");
        pautaCancelada.setCancelado(true);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListar() {
        when(pautaRepository.listarPautasComVotos()).thenReturn(List.of(pauta));
        Pauta pautaTest = pautaService.listar().getFirst();
        assertEquals(pauta.getId(), pautaTest.getId());
        assertEquals(pauta.getTitulo(), pautaTest.getTitulo());
        assertEquals(pauta.getDescricao(), pautaTest.getDescricao());
    }

    @Test
    void testEncontraPorId() {
        when(pautaRepository.encontrarPautasPorIdComVotos(pauta.getId())).thenReturn(Optional.of(pauta));
        Pauta pautaTest = pautaService.encontraPorId(pauta.getId());
        assertEquals(pauta.getId(), pautaTest.getId());
        assertEquals(pauta.getTitulo(), pautaTest.getTitulo());
        assertEquals(pauta.getDescricao(), pautaTest.getDescricao());
    }

    @Test
    void testEncontraPorIdNotFound() {
        when(pautaRepository.encontrarPautasPorIdComVotos(pauta.getId())).thenReturn(Optional.empty());
        Long idPauta = pauta.getId();
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> pautaService.encontraPorId(idPauta));
        String erroEsperado = "Pauta não encontrada.";
        String erro = exception.getMessage();
        assertEquals(erroEsperado, erro);
    }

    @Test
    void testPautaCancelada() {
        pauta.setTempoLimiteEmAberto(LocalDateTime.now().minusMinutes(2L));
        when(pautaRepository.encontrarPautasPorIdComVotos(pautaCancelada.getId())).thenReturn(Optional.of(pautaCancelada));
        Long idPauta = pautaCancelada.getId();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> pautaService.ativarVotacao(idPauta));
        String erroEsperado = "Pauta cancelada.";
        String erro = exception.getMessage();
        assertEquals(erroEsperado, erro);
    }

    @Test
    void testPautaVotacaoEncerrada() {
        pauta.setTempoLimiteEmAberto(LocalDateTime.now().minusMinutes(2L));
        when(pautaRepository.encontrarPautasPorIdComVotos(pauta.getId())).thenReturn(Optional.of(pauta));
        Long idPauta = pauta.getId();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> pautaService.ativarVotacao(idPauta));
        String erroEsperado = "Pauta encerrada.";
        String erro = exception.getMessage();
        assertEquals(erroEsperado, erro);
    }

    @Test
    void testPautaEmVotacao() {
        pauta.setTempoLimiteEmAberto(LocalDateTime.now().plusMinutes(30L));
        when(pautaRepository.encontrarPautasPorIdComVotos(pauta.getId())).thenReturn(Optional.of(pauta));
        Long idPauta = pauta.getId();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> pautaService.ativarVotacao(idPauta));
        String erroEsperado = "Pauta está em votação.";
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
