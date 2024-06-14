package com.luiz.decisoespautas.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class VotoSessaoPauta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean votoPositivo;

    @Column
    private String cpf;

    @ManyToOne
    @JoinColumn(name = "id-pauta", nullable = false)
    private Pauta pauta;
}
