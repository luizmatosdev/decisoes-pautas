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
        return sessaoPautaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sessao Pauta não encontrada"));
    }

    public SessaoPauta save(SessaoPauta sessaoPauta) {
        Pauta pauta = pautaService.find(sessaoPauta.getPauta().getId());
        sessaoPauta.setPauta(pauta);
        sessaoPauta.setTempoEmAberto(LocalDateTime.now().plusMinutes(sessaoPauta.getMinutosEmAberto() == null ? 5 : sessaoPauta.getMinutosEmAberto()));
        return sessaoPautaRepository.save(sessaoPauta);
    }
}
