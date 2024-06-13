package com.luiz.decisoespautas.controllers;

import com.luiz.decisoespautas.entities.SessaoPauta;
import com.luiz.decisoespautas.service.SessaoPautaService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessao-pauta")
public class SessaoPautaController {
    @Autowired
    private SessaoPautaService sessaoPautaService;

    @GetMapping
    public SessaoPauta find(@PathParam("id") Long id) {
        return sessaoPautaService.find(id);
    }

    @PostMapping
    public SessaoPauta save(@RequestBody SessaoPauta sessaoPauta) {
        return sessaoPautaService.save(sessaoPauta);
    }
}
