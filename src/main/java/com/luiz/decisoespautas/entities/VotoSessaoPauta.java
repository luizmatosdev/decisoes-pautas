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

    @ManyToOne
    @JoinColumn(name = "id-sessao-pauta", nullable = false)
    private SessaoPauta sessaoPauta;

    @ManyToOne
    @JoinColumn(name = "cpf-usuario", nullable = false, unique = true)
    private Usuario usuario;
}
