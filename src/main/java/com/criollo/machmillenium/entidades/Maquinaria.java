package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.Duration;

@Entity
@Table(name = "maquinarias")
public class Maquinaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tipo_maquinaria_id")
    private TipoMaquinaria tipoMaquinaria;

    private String nombre;

    private Duration tiempoEstimadoDeUso;
    private Double costoPorTiempoDeUso;

    private LocalDateTime creado;
    private LocalDateTime modificado;
    private LocalDateTime eliminado;
}
