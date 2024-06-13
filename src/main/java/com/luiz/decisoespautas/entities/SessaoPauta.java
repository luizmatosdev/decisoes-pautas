package com.luiz.decisoespautas.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

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
    private Date tempoEmAberto;
}
