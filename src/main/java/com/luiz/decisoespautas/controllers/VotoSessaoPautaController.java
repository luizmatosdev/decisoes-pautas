package com.luiz.decisoespautas.controllers;

import com.luiz.decisoespautas.entities.VotoSessaoPauta;
import com.luiz.decisoespautas.service.VotoSessaoPautaService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voto")
public class VotoSessaoPautaController {
    @Autowired
    private VotoSessaoPautaService votoSessaoPautaService;

    @GetMapping
    public VotoSessaoPauta find(@PathParam("id") Long id) {
        return votoSessaoPautaService.find(id);
    }

    @PostMapping
    public VotoSessaoPauta save(@RequestBody VotoSessaoPauta votoSessaoPauta) {
        return votoSessaoPautaService.save(votoSessaoPauta);
    }
}
