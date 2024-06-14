package com.luiz.decisoespautas.controllers;

import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.service.PautaService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pauta")
public class PautaController {

    @Autowired
    private PautaService pautaService;

    @GetMapping
    public List<Pauta> getAllPautas() {
        return pautaService.findAll();
    }

    @PostMapping
    public Pauta save(@RequestBody Pauta pauta) {
        return pautaService.save(pauta);
    }

    @PatchMapping
    public void ativarVotacao(@PathParam("id") Long id) {
        pautaService.ativarVotacao(id);
    }

    // resetar uma votação de pauta?
}
