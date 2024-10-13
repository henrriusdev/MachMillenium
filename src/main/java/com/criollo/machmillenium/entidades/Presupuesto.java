package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.Duration;

@Entity
@Table(name = "presupuestos")
public class Presupuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private String direccion;
    private String nombre;

    private Duration tiempoEstimado;
    private Double costo;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private LocalDateTime creado;
    private LocalDateTime modificado;
    private LocalDateTime eliminado;
}
