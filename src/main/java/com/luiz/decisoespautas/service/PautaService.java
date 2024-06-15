package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.repositories.PautaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PautaService {

    @Autowired
    private PautaRepository pautaRepository;

    public List<Pauta> listar() {
        // Fazer uma pesquisa paginada?
        return pautaRepository.listarPautasComVotos();
    }

    public Pauta encontraPorId(Long id) {
        return pautaRepository.encontrarPautasPorIdComVotos(id).orElseThrow(() -> new EntityNotFoundException("Pauta não encontrada."));
    }

    public Pauta save(Pauta pauta) {
        return pautaRepository.save(pauta);
    }

    public void ativarVotacao(Long id) {
        Pauta pauta = encontraPorId(id);
        validacaoPauta(pauta);
        pauta.setTempoLimiteEmAberto(LocalDateTime.now().plusMinutes(pauta.getMinutosEmAberto() == null ? 1 : pauta.getMinutosEmAberto()));
        pautaRepository.save(pauta);
    }

    public void cancelarPauta(Long id, String motivo) {
        Pauta pauta = encontraPorId(id);
        if (pauta.isCancelado()) {
            throw new IllegalArgumentException("Pauta já cancelada.");
        }
        pauta.setCancelado(true);
        pauta.setMotivoCancelamento(motivo);
        pautaRepository.save(pauta);
    }

    private static void validacaoPauta(Pauta pauta) {
        if (pauta.isCancelado()) {
            throw new IllegalArgumentException("Pauta cancelada.");
        }
        if (pauta.getTempoLimiteEmAberto() != null) {
            if (pauta.getTempoLimiteEmAberto().isAfter(LocalDateTime.now())) {
                throw new IllegalArgumentException("Pauta está em votação.");
            } else {
                throw new IllegalArgumentException("Pauta encerrada.");
            }
        }
    }

}
