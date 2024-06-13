package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.entities.SessaoPauta;
import com.luiz.decisoespautas.repositories.SessaoPautaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SessaoPautaService {
    @Autowired
    private SessaoPautaRepository sessaoPautaRepository;
    @Autowired
    private PautaService pautaService;

    public SessaoPauta find(Long id) {
        SessaoPauta sessaoPauta = sessaoPautaRepository.findById(id).orElse(null);
        if (sessaoPauta == null) {
            throw new EntityNotFoundException("Sessão Pauta não encontrada");
        }
        return sessaoPauta;
    }

    public SessaoPauta save(SessaoPauta sessaoPauta) {
        Pauta pauta = pautaService.find(sessaoPauta.getPauta().getId());
        if (pauta == null) {
            throw new EntityNotFoundException("Pauta não encontrada");
        }
        sessaoPauta.setPauta(pauta);
        sessaoPauta.setTempoEmAberto(LocalDateTime.now().plusMinutes(sessaoPauta.getMinutosEmAberto() == null ? 1 : sessaoPauta.getMinutosEmAberto()));
        return sessaoPautaRepository.save(sessaoPauta);
    }
}
