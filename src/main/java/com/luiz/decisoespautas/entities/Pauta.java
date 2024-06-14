package com.luiz.decisoespautas.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table
public class Pauta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Size(max = 255)
    private String titulo;

    @Column(columnDefinition = "text")
    private String descricao;

    @Column
    private LocalDateTime tempoLimiteEmAberto;

    @Column
    private Long minutosEmAberto;
}
