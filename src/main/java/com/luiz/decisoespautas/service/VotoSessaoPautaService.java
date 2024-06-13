package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.entities.SessaoPauta;
import com.luiz.decisoespautas.entities.VotoSessaoPauta;
import com.luiz.decisoespautas.repositories.VotoSessaoPautaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VotoSessaoPautaService {
    @Autowired
    private VotoSessaoPautaRepository votoSessaoPautaRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private SessaoPautaService sessaoPautaService;

    public VotoSessaoPauta find(Long id) {
        return votoSessaoPautaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Voto não encontrado"));
    }

    public VotoSessaoPauta save(VotoSessaoPauta votoSessaoPauta) {
        // Validar se Sessão da Pauta existe
        SessaoPauta sessaoPauta = sessaoPautaService.find(votoSessaoPauta.getSessaoPauta().getId());
        validaSeSessaoIncerrada(sessaoPauta);
        votoSessaoPauta.setSessaoPauta(sessaoPauta);

        usuarioService.criarUsuario(votoSessaoPauta.getUsuario());
        if (seUsuarioJaVotouNaSessao(votoSessaoPauta.getSessaoPauta().getId(), votoSessaoPauta.getUsuario().getCpf())) {
            throw new IllegalArgumentException("Usuário já votou na sessão da pauta");
        }
        return votoSessaoPautaRepository.save(votoSessaoPauta);
    }

    private boolean seUsuarioJaVotouNaSessao(Long idSessaoPauta, String cpf) {
        int quantidadeVoto = votoSessaoPautaRepository.existeVotoUsuarioNaSessao(idSessaoPauta, cpf);
        return quantidadeVoto != 0;
    }

    private static void validaSeSessaoIncerrada(SessaoPauta sessaoPauta) {
        if (sessaoPauta.getTempoEmAberto().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Sessão encerrada");
        }
    }
}
