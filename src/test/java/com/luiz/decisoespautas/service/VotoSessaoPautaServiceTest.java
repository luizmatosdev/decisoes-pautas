package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.entities.SessaoPauta;
import com.luiz.decisoespautas.entities.Usuario;
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

    Usuario usuario;
    SessaoPauta sessaoPauta;
    Pauta pauta;
    VotoSessaoPauta votoSessaoPauta;

    @InjectMocks
    private VotoSessaoPautaService votoSessaoPautaService;
    @Mock
    private VotoSessaoPautaRepository votoSessaoPautaRepository;
    @Mock
    private SessaoPautaService sessaoPautaService;
    @Mock
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setCpf("01234567890");

        pauta = new Pauta();
        pauta.setId(1L);
        pauta.setTitulo("Titulo");
        pauta.setDescricao("Descrição");

        sessaoPauta = new SessaoPauta();
        sessaoPauta.setId(2L);
        sessaoPauta.setPauta(pauta);
        sessaoPauta.setMinutosEmAberto(10L);
        sessaoPauta.setTempoLimiteEmAberto(LocalDateTime.now().plusMinutes(10));

        votoSessaoPauta = new VotoSessaoPauta();
        votoSessaoPauta.setId(5L);
        votoSessaoPauta.setVotoPositivo(true);
        votoSessaoPauta.setUsuario(usuario);
        votoSessaoPauta.setSessaoPauta(sessaoPauta);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFind() {
        when(votoSessaoPautaRepository.findById(votoSessaoPauta.getId())).thenReturn(Optional.of(votoSessaoPauta));
        VotoSessaoPauta votoSessaoPautaTest = votoSessaoPautaService.find(votoSessaoPauta.getId());

        assertEquals(votoSessaoPauta.getId(), votoSessaoPautaTest.getId());
        assertEquals(votoSessaoPauta.getVotoPositivo(), votoSessaoPautaTest.getVotoPositivo());

        SessaoPauta sessaoPautaTest = votoSessaoPautaTest.getSessaoPauta();
        assertEquals(sessaoPauta.getId(), sessaoPautaTest.getId());

        Pauta pautaTest = sessaoPautaTest.getPauta();
        assertEquals(pauta.getId(), pautaTest.getId());
        assertEquals(pauta.getTitulo(), pautaTest.getTitulo());
        assertEquals(pauta.getDescricao(), pautaTest.getDescricao());

        Usuario usuarioFind = votoSessaoPauta.getUsuario();
        assertEquals(usuario.getCpf(), usuarioFind.getCpf());
    }


    @Test
    void testFindNotFound() {
        when(votoSessaoPautaRepository.findById(votoSessaoPauta.getId())).thenReturn(Optional.empty());
        Long idPauta = votoSessaoPauta.getId();
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            votoSessaoPautaService.find(idPauta);
        });

        String erroEsperado = "Voto não encontrado";
        String erro = exception.getMessage();
        assertEquals(erroEsperado, erro);
    }

    @Test
    void testSave() {
        when(sessaoPautaService.find(votoSessaoPauta.getSessaoPauta().getId())).thenReturn(sessaoPauta);
        when(votoSessaoPautaRepository.existeVotoUsuarioNaSessao(sessaoPauta.getId(), usuario.getCpf())).thenReturn(0);
        when(votoSessaoPautaRepository.save(votoSessaoPauta)).thenReturn(votoSessaoPauta);

        VotoSessaoPauta votoSessaoPautaTest = votoSessaoPautaService.save(votoSessaoPauta);

        assertEquals(votoSessaoPauta.getId(), votoSessaoPautaTest.getId());
        assertEquals(votoSessaoPauta.getVotoPositivo(), votoSessaoPautaTest.getVotoPositivo());

        SessaoPauta sessaoPautaTest = votoSessaoPautaTest.getSessaoPauta();
        assertEquals(sessaoPauta.getId(), sessaoPautaTest.getId());

        Pauta pautaTest = sessaoPautaTest.getPauta();
        assertEquals(pauta.getId(), pautaTest.getId());
        assertEquals(pauta.getTitulo(), pautaTest.getTitulo());
        assertEquals(pauta.getDescricao(), pautaTest.getDescricao());

        Usuario usuarioFind = votoSessaoPauta.getUsuario();
        assertEquals(usuario.getCpf(), usuarioFind.getCpf());

    }

    @Test
    void testSaveUsuarioJaVotou() {
        when(sessaoPautaService.find(votoSessaoPauta.getSessaoPauta().getId())).thenReturn(sessaoPauta);
        when(votoSessaoPautaRepository.existeVotoUsuarioNaSessao(sessaoPauta.getId(), usuario.getCpf())).thenReturn(1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            votoSessaoPautaService.save(votoSessaoPauta);
        });

        String erroEsperado = "Usuário já votou na sessão da pauta";
        String erro = exception.getMessage();

        assertEquals(erroEsperado, erro);
    }

    @Test
    void testSaveTempoLimiteEsgotado() {
        sessaoPauta.setTempoLimiteEmAberto(LocalDateTime.now().minusMinutes(30));
        when(sessaoPautaService.find(votoSessaoPauta.getSessaoPauta().getId())).thenReturn(sessaoPauta);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            votoSessaoPautaService.save(votoSessaoPauta);
        });

        String erroEsperado = "Sessão encerrada";
        String erro = exception.getMessage();

        assertEquals(erroEsperado, erro);
    }
}
