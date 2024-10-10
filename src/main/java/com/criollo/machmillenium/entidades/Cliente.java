package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String cedula;

    @Column(length = 22)
    private String telefono;

    @Column(length = 100)
    private String direccion;

    private Integer edad;

    @Column(length = 100)
    private String correo;

    private String sexo;
    private Boolean activo;

    private LocalDateTime creado;
    private LocalDateTime modificado;
    private LocalDateTime eliminado;
}
