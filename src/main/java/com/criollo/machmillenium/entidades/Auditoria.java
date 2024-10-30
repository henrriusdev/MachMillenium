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

    public Auditoria() {
    }

    public void registrar(String accion, String detalle) {
        this.realizado = LocalDateTime.now();
        this.accion = accion;
        this.detalle = detalle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public LocalDateTime getRealizado() {
        return realizado;
    }

    public void setRealizado(LocalDateTime realizado) {
        this.realizado = realizado;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}
