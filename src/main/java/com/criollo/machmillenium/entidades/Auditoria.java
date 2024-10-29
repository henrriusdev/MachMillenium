package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table
public class Auditoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String personal;
    private LocalDateTime realizado;
    private String accion;
    private String detalle;

    public Auditoria(String personal) {
        this.personal = personal;
    }

    public void registrar(String accion, String detalle) {
        this.realizado = LocalDateTime.now();
        this.accion = accion;
        this.detalle = detalle;
    }
}
