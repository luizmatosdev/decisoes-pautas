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

    public List<Pauta> getAllPautas() {
        // Fazer uma pesquisa paginada?
        return pautaRepository.findAll();
    }

    public Pauta createPauta(Pauta pauta) {
        pautaRepository.save(pauta);
        return pauta;
    }
}
