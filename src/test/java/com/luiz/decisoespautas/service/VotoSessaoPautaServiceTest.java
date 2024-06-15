package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.entities.VotoSessaoPauta;
import com.luiz.decisoespautas.repositories.VotoSessaoPautaRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class VotoSessaoPautaServiceTest {

    Pauta pauta;
    Pauta pautaCancelada;
    VotoSessaoPauta votoSessaoPauta;

    @InjectMocks
    private VotoSessaoPautaService votoSessaoPautaService;
    @Mock
    private VotoSessaoPautaRepository votoSessaoPautaRepository;
    @Mock
    private PautaService pautaService;

    @BeforeEach
    void setUp() {
        pauta = new Pauta();
        pauta.setId(1L);
        pauta.setTitulo("Titulo");
        pauta.setDescricao("Descrição");
        pauta.setMinutosEmAberto(10L);
        pauta.setTempoLimiteEmAberto(LocalDateTime.now().plusMinutes(10));


        pautaCancelada = new Pauta();
        pautaCancelada.setId(2L);
        pautaCancelada.setTitulo("Titulo Cancelada");
        pautaCancelada.setDescricao("Descrição Cancelada");
        pautaCancelada.setCancelado(true);

        votoSessaoPauta = new VotoSessaoPauta();
        votoSessaoPauta.setId(5L);
        votoSessaoPauta.setVotoPositivo(true);
        votoSessaoPauta.setCpf("01234567890");
        votoSessaoPauta.setPauta(pauta);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testEncontraPorId() {
        when(votoSessaoPautaRepository.findById(votoSessaoPauta.getId())).thenReturn(Optional.of(votoSessaoPauta));
        VotoSessaoPauta votoSessaoPautaTest = votoSessaoPautaService.encontraPorId(votoSessaoPauta.getId());

        assertEquals(votoSessaoPauta.getId(), votoSessaoPautaTest.getId());
        assertEquals(votoSessaoPauta.getVotoPositivo(), votoSessaoPautaTest.getVotoPositivo());
        assertEquals(votoSessaoPauta.getCpf(), votoSessaoPautaTest.getCpf());

        Pauta pautaTest = votoSessaoPautaTest.getPauta();
        assertEquals(pauta.getId(), pautaTest.getId());
        assertEquals(pauta.getTitulo(), pautaTest.getTitulo());
        assertEquals(pauta.getDescricao(), pautaTest.getDescricao());
    }


    @Test
    void testEncontraPorIdNotFound() {
        when(votoSessaoPautaRepository.findById(votoSessaoPauta.getId())).thenReturn(Optional.empty());
        Long idPauta = votoSessaoPauta.getId();
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            votoSessaoPautaService.encontraPorId(idPauta);
        });

        String erroEsperado = "Voto não encontrado.";
        String erro = exception.getMessage();
        assertEquals(erroEsperado, erro);
    }

    @Test
    void testSave() {
        when(pautaService.encontraPorId(votoSessaoPauta.getPauta().getId())).thenReturn(pauta);
        when(votoSessaoPautaRepository.existeVotoUsuarioNaSessao(pauta.getId(), votoSessaoPauta.getCpf())).thenReturn(0);
        when(votoSessaoPautaRepository.save(votoSessaoPauta)).thenReturn(votoSessaoPauta);

        VotoSessaoPauta votoSessaoPautaTest = votoSessaoPautaService.save(votoSessaoPauta);

        assertEquals(votoSessaoPauta.getId(), votoSessaoPautaTest.getId());
        assertEquals(votoSessaoPauta.getVotoPositivo(), votoSessaoPautaTest.getVotoPositivo());
        assertEquals(votoSessaoPauta.getCpf(), votoSessaoPautaTest.getCpf());

        Pauta pautaTest = votoSessaoPautaTest.getPauta();
        assertEquals(pauta.getId(), pautaTest.getId());
        assertEquals(pauta.getTitulo(), pautaTest.getTitulo());
        assertEquals(pauta.getDescricao(), pautaTest.getDescricao());
    }

    @Test
    void testSaveCpfInvalido() {
        votoSessaoPauta.setCpf("invalido");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            votoSessaoPautaService.save(votoSessaoPauta);
        });

        String erroEsperado = "CPF inválido.";
        String erro = exception.getMessage();

        assertEquals(erroEsperado, erro);
    }

    @Test
    void testSaveUsuarioJaVotou() {
        when(pautaService.encontraPorId(votoSessaoPauta.getPauta().getId())).thenReturn(pauta);
        when(votoSessaoPautaRepository.existeVotoUsuarioNaSessao(pauta.getId(), votoSessaoPauta.getCpf())).thenReturn(1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            votoSessaoPautaService.save(votoSessaoPauta);
        });

        String erroEsperado = "Usuário já votou nesta pauta.";
        String erro = exception.getMessage();

        assertEquals(erroEsperado, erro);
    }

    @Test
    void testSavePautaCancelada() {
        pauta.setTempoLimiteEmAberto(LocalDateTime.now().minusMinutes(30));
        when(pautaService.encontraPorId(votoSessaoPauta.getPauta().getId())).thenReturn(pautaCancelada);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            votoSessaoPautaService.save(votoSessaoPauta);
        });

        String erroEsperado = "Pauta cancelada.";
        String erro = exception.getMessage();

        assertEquals(erroEsperado, erro);
    }

    @Test
    void testSaveVotoPautaNaoIniciada() {
        pauta.setTempoLimiteEmAberto(null);
        when(pautaService.encontraPorId(votoSessaoPauta.getPauta().getId())).thenReturn(pauta);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            votoSessaoPautaService.save(votoSessaoPauta);
        });

        String erroEsperado = "Pauta não iniciada.";
        String erro = exception.getMessage();

        assertEquals(erroEsperado, erro);
    }

    @Test
    void testSaveTempoLimiteEsgotado() {
        pauta.setTempoLimiteEmAberto(LocalDateTime.now().minusMinutes(30));
        when(pautaService.encontraPorId(votoSessaoPauta.getPauta().getId())).thenReturn(pauta);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            votoSessaoPautaService.save(votoSessaoPauta);
        });

        String erroEsperado = "Pauta encerrada.";
        String erro = exception.getMessage();

        assertEquals(erroEsperado, erro);
    }
}
