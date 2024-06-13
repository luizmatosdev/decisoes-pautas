package com.luiz.decisoespautas.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class Pauta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String titulo;

    @Column
    private String descricao;
}
