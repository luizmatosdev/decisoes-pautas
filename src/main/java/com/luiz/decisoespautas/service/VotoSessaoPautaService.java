package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.entities.VotoSessaoPauta;
import com.luiz.decisoespautas.repositories.VotoSessaoPautaRepository;
import com.luiz.decisoespautas.utils.ValidaCpf;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VotoSessaoPautaService {
    @Autowired
    private VotoSessaoPautaRepository votoSessaoPautaRepository;
    @Autowired
    private PautaService pautaService;

    public VotoSessaoPauta encontraPorId(Long id) {
        return votoSessaoPautaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Voto não encontrado."));
    }

    public VotoSessaoPauta save(VotoSessaoPauta votoSessaoPauta) {
        validaCpf(votoSessaoPauta.getCpf());

        Pauta pauta = pautaService.encontraPorId(votoSessaoPauta.getPauta().getId());
        validaPauta(pauta);
        votoSessaoPauta.setPauta(pauta);

        validaSeUsuarioJaVotouNaSessao(votoSessaoPauta.getPauta().getId(), votoSessaoPauta.getCpf());
        return votoSessaoPautaRepository.save(votoSessaoPauta);
    }

    private void validaCpf(String cpf) {
        if (!ValidaCpf.isCPF(cpf)) {
            throw new IllegalArgumentException("CPF inválido.");
        }
    }

    private void validaSeUsuarioJaVotouNaSessao(Long idPauta, String cpf) {
        int quantidadeVoto = votoSessaoPautaRepository.existeVotoUsuarioNaSessao(idPauta, cpf);
        if (quantidadeVoto != 0) {
            throw new IllegalArgumentException("Usuário já votou nesta pauta.");
        }
    }

    private void validaPauta(Pauta pauta) {
        validaSePautaCancelada(pauta);
        validaSePautaNaoIniciada(pauta);
        validaSePautaEncerrada(pauta);
    }

    private void validaSePautaCancelada(Pauta pauta) {
        if (pauta.isCancelado()) {
            throw new IllegalArgumentException("Pauta cancelada.");
        }
    }

    private void validaSePautaNaoIniciada(Pauta pauta) {
        if (pauta.getTempoLimiteEmAberto() == null) {
            throw new IllegalArgumentException("Pauta não iniciada.");
        }
    }

    private void validaSePautaEncerrada(Pauta pauta) {
        if (pauta.getTempoLimiteEmAberto().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Pauta encerrada.");
        }
    }
}
