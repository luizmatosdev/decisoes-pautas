package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.entities.SessaoPauta;
import com.luiz.decisoespautas.repositories.SessaoPautaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class SessaoPautaServiceTest {

    SessaoPauta sessaoPauta;
    Pauta pauta;

    @InjectMocks
    private SessaoPautaService sessaoPautaService;
    @Mock
    private SessaoPautaRepository sessaoPautaRepository;
    @Mock
    private PautaService pautaService;

    @BeforeEach
    void setUp() {
        pauta = new Pauta();
        pauta.setId(1L);
        pauta.setTitulo("Titulo");
        pauta.setDescricao("Descrição");

        sessaoPauta = new SessaoPauta();
        sessaoPauta.setId(2L);
        sessaoPauta.setPauta(pauta);
        sessaoPauta.setMinutosEmAberto(10L);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFind() {
        when(sessaoPautaRepository.findById(sessaoPauta.getId())).thenReturn(Optional.of(sessaoPauta));

        SessaoPauta sessaoPautaTest = sessaoPautaService.find(sessaoPauta.getId());
        assertEquals(sessaoPauta.getId(), sessaoPautaTest.getId());

        Pauta pautaTest = sessaoPautaTest.getPauta();
        assertEquals(pauta.getId(), pautaTest.getId());
        assertEquals(pauta.getTitulo(), pautaTest.getTitulo());
        assertEquals(pauta.getDescricao(), pautaTest.getDescricao());
    }

    @Test
    void testFindNotFound() {
        when(sessaoPautaRepository.findById(sessaoPauta.getId())).thenReturn(Optional.empty());
        Long idPauta = sessaoPauta.getId();
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            sessaoPautaService.find(idPauta);
        });

        String erroEsperado = "Sessao Pauta não encontrada";
        String erro = exception.getMessage();
        assertEquals(erroEsperado, erro);
    }

    @Test
    void testSave() {

        when(pautaService.find(pauta.getId())).thenReturn(pauta);
        when(sessaoPautaRepository.save(sessaoPauta)).thenReturn(sessaoPauta);
        SessaoPauta sessaoPautaTest = sessaoPautaService.save(sessaoPauta);

        assertEquals(sessaoPauta.getId(), sessaoPautaTest.getId());
        // Testa se o tempo em aberto está correto
        LocalDateTime tempoLimite = sessaoPautaTest.getTempoLimiteEmAberto();
        LocalDateTime tempoAgora = LocalDateTime.now().plusMinutes(sessaoPauta.getMinutosEmAberto());
        long minutosEmAberto = sessaoPauta.getMinutosEmAberto();
        assertTrue(tempoLimite.isBefore(tempoAgora.plusMinutes(minutosEmAberto)) || tempoLimite.equals(tempoAgora.plusMinutes(minutosEmAberto)));

        Pauta pautaTest = sessaoPautaTest.getPauta();
        assertEquals(pauta.getId(), pautaTest.getId());
        assertEquals(pauta.getTitulo(), pautaTest.getTitulo());
        assertEquals(pauta.getDescricao(), pautaTest.getDescricao());
    }
}
