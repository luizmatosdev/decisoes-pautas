package com.luiz.decisoespautas.controllers;

import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.service.PautaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pauta")
public class PautaController {

    @Autowired
    private PautaService pautaService;

    @PostMapping(consumes = "application/json")
    public Pauta createPauta(@RequestBody Pauta pauta) {
        return pautaService.createPauta(pauta);
    }

    @GetMapping
    public List<Pauta> getAllPautas() {
        return pautaService.getAllPautas();
    }
}
