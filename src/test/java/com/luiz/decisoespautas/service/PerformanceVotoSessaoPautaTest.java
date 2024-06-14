package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.entities.VotoSessaoPauta;
import com.luiz.decisoespautas.repositories.VotoSessaoPautaRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PerformanceVotoSessaoPautaTest {

    Pauta pauta;
    VotoSessaoPauta votoSessaoPauta;

    @InjectMocks
    private VotoSessaoPautaService votoSessaoPautaService;
    @Mock
    private VotoSessaoPautaRepository votoSessaoPautaRepository;
    @Mock
    private PautaService pautaService;

    @BeforeAll
    public void init() {

        pauta = new Pauta();
        pauta.setId(1L);
        pauta.setTitulo("Titulo");
        pauta.setDescricao("Descrição");
        pauta.setMinutosEmAberto(10L);
        pauta.setTempoLimiteEmAberto(LocalDateTime.now().plusMinutes(10));

        votoSessaoPauta = new VotoSessaoPauta();
        votoSessaoPauta.setId(5L);
        votoSessaoPauta.setVotoPositivo(true);
        votoSessaoPauta.setCpf("01234567890");
        votoSessaoPauta.setPauta(pauta);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
    void testPerformanceVotoSessaoPauta() {

        when(pautaService.find(votoSessaoPauta.getPauta().getId())).thenReturn(pauta);
        when(votoSessaoPautaRepository.existeVotoUsuarioNaSessao(pauta.getId(), votoSessaoPauta.getCpf())).thenReturn(0);
        when(votoSessaoPautaRepository.save(any(VotoSessaoPauta.class))).thenReturn(null);

        try {
            for (int i = 0; i < 500; i++) {
                votoSessaoPautaService.save(votoSessaoPauta);
            }
        } catch (Exception e) {
            Assertions.fail("Não deve conter erros");
        }
    }
}
