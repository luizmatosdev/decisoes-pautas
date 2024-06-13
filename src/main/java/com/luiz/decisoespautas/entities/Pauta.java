package com.luiz.decisoespautas.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class Pauta {
    @Id
    private Integer id;
    private String titulo;
    private String descricao;
}
