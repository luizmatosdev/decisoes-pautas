package com.luiz.decisoespautas.service;

import com.luiz.decisoespautas.entities.Pauta;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PautaService {

    public List<Pauta> getAllPautas() {
        // Fazer uma pesquisa paginada?
        return List.of(new Pauta());
    }

    public Pauta createPauta(Pauta pauta) {
        pauta.setId(1);
        return pauta;
    }
}
