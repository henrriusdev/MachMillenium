package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "maquinaria")
public class Maquinaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tipo_maquinaria_id")
    private TipoMaquinaria tipoMaquinaria;

    private Double tiempoEstimadoDeUso;
    private Double costoPorTiempoDeUso;

    private LocalDateTime creado;
    private LocalDateTime modificado;
    private LocalDateTime eliminado;
}
