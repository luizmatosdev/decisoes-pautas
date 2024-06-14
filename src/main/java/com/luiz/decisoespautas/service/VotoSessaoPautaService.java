package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.entities.Pauta;
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
    private PautaService pautaService;

    public VotoSessaoPauta find(Long id) {
        return votoSessaoPautaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Voto não encontrado"));
    }

    public VotoSessaoPauta save(VotoSessaoPauta votoSessaoPauta) {
        // Validar se Sessão da Pauta existe
        Pauta pauta = pautaService.find(votoSessaoPauta.getPauta().getId());
        validaSeSessaoEncerrada(pauta);
        votoSessaoPauta.setPauta(pauta);

        validaCpf(votoSessaoPauta.getCpf());
        if (seUsuarioJaVotouNaSessao(votoSessaoPauta.getId(), votoSessaoPauta.getCpf())) {
            throw new IllegalArgumentException("Usuário já votou na sessão da pauta");
        }
        return votoSessaoPautaRepository.save(votoSessaoPauta);
    }

    private boolean seUsuarioJaVotouNaSessao(Long idPauta, String cpf) {
        int quantidadeVoto = votoSessaoPautaRepository.existeVotoUsuarioNaSessao(idPauta, cpf);
        return quantidadeVoto != 0;
    }

    private void validaCpf(String cpf) {
        if (cpf.length() != 11) {
            throw new IllegalArgumentException("CPF inválido");
        }
    }

    private void validaSeSessaoEncerrada(Pauta pauta) {
        if (pauta.getTempoLimiteEmAberto().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Sessão encerrada");
        }
    }
}
