package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.repositories.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return pautaRepository.findById(id).orElse(null);
    }

    public Pauta save(Pauta pauta) {
        pautaRepository.save(pauta);
        return pauta;
    }
}
