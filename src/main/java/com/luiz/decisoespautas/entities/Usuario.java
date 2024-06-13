package com.luiz.decisoespautas.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class Usuario {

    @Id
    @Column
    private String cpf;

}
