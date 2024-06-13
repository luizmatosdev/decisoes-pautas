package com.luiz.decisoespautas.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table
@Data
public class SessaoPauta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id-pauta", nullable = false)
    private Pauta pauta;

    @Column
    private LocalDateTime tempoLimiteEmAberto;

    @Transient
    private Long minutosEmAberto;
}
