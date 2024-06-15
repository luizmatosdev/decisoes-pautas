package com.luiz.decisoespautas.controllers;

import com.luiz.decisoespautas.dtos.CancelamentoPautaRequestDTO;
import com.luiz.decisoespautas.entities.Pauta;
import com.luiz.decisoespautas.service.PautaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pauta")
@Tag(name = "Pauta", description = "Endpoints para gerenciamento de pautas")
public class PautaController {

    @Autowired
    private PautaService pautaService;

    @Operation(summary = "Lista todas as pautas")
    @GetMapping
    public List<Pauta> listar() {
        return pautaService.listar();
    }

    @Operation(summary = "Lista todas as pautas")
    @GetMapping("/{id}")
    public Pauta getAllPautas(@PathVariable("id") Long id) {
        return pautaService.encontraPorId(id);
    }

    @Operation(summary = "Salva informações de uma pauta")
    @PostMapping
    public Pauta save(@RequestBody Pauta pauta) {
        return pautaService.save(pauta);
    }

    @Operation(summary = "Cancela uma pauta")
    @PostMapping("/cancelamento")
    public void cancelarPauta(@RequestBody CancelamentoPautaRequestDTO cancelamentoPautaRequestDTO) {
        pautaService.cancelarPauta(cancelamentoPautaRequestDTO.getId(), cancelamentoPautaRequestDTO.getMotivoCancelamento());
    }

    @Operation(summary = "Habilita a votação de uma pauta")
    @PatchMapping()
    public void ativarVotacao(@PathParam("id") Long id) {
        pautaService.ativarVotacao(id);
    }
}
