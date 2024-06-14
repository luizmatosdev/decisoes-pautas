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

    public List<Pauta> findAll() {
        // Fazer uma pesquisa paginada?
        return pautaRepository.findAll();
    }

    public Pauta find(Long id) {
        return pautaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Pauta não encontrada"));
    }

    public Pauta save(Pauta pauta) {
        return pautaRepository.save(pauta);
    }

    public void ativarVotacao(Long id) {
        Pauta pauta = find(id);
        if (pauta.getTempoLimiteEmAberto() != null) {
            if (pauta.getTempoLimiteEmAberto().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Pauta está em votação");
            } else {
                throw new IllegalArgumentException("Pauta encerrada");
            }
        }
        pauta.setTempoLimiteEmAberto(LocalDateTime.now().plusMinutes(pauta.getMinutosEmAberto() == null ? 1 : pauta.getMinutosEmAberto()));
        pautaRepository.save(pauta);
    }
}
