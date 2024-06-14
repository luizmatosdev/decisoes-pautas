package com.luiz.decisoespautas.controllers;

import com.luiz.decisoespautas.entities.VotoSessaoPauta;
import com.luiz.decisoespautas.service.VotoSessaoPautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/voto")
@Tag(name = "Voto da Pauta", description = "Endpoints para gerenciamento de votos de uma pauta")
public class VotoSessaoPautaController {
    @Autowired
    private VotoSessaoPautaService votoSessaoPautaService;

    @Operation(summary = "Encontra um voto pelo id")
    @GetMapping
    public VotoSessaoPauta find(@PathParam("id") Long id) {
        return votoSessaoPautaService.find(id);
    }

    @Operation(summary = "Salva um voto de uma pauta")
    @PostMapping
    public VotoSessaoPauta save(@RequestBody VotoSessaoPauta votoSessaoPauta) {
        return votoSessaoPautaService.save(votoSessaoPauta);
    }
}
