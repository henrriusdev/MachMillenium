package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tipo_maquinaria")
public class TipoMaquinaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private LocalDateTime creado;
    private LocalDateTime modificado;
    private LocalDateTime eliminado;
}