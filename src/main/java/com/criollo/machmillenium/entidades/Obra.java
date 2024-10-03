package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "obras")
public class Obra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tipo_de_obra_id")
    private TipoObra tipoObra;

    private Double tiempoEstimado;
    private Double area;
    private Double costoEstimado;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private Double presupuestoId;
    private LocalDateTime creado;
    private LocalDateTime modificado;
    private LocalDateTime eliminado;
}

