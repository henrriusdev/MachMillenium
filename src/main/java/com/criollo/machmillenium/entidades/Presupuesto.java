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

    private Duration tiempoEstimado;
    private Double costo;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private LocalDateTime creado;
    private LocalDateTime modificado;
    private LocalDateTime eliminado;

    public Presupuesto() {
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public Presupuesto(String descripcion, String direccion, Duration tiempoEstimado, Double costo, Cliente cliente) {
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.tiempoEstimado = tiempoEstimado;
        this.costo = costo;
        this.cliente = cliente;
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public Presupuesto(Long id, String descripcion, String direccion, Duration tiempoEstimado, Double costo, Cliente cliente) {
        this.id = id;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.tiempoEstimado = tiempoEstimado;
        this.costo = costo;
        this.cliente = cliente;
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Duration getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(Duration tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getCreado() {
        return creado;
    }

    public void setCreado(LocalDateTime creado) {
        this.creado = creado;
    }

    public LocalDateTime getModificado() {
        return modificado;
    }

    public void setModificado(LocalDateTime modificado) {
        this.modificado = modificado;
    }

    public LocalDateTime getEliminado() {
        return eliminado;
    }

    public void setEliminado(LocalDateTime eliminado) {
        this.eliminado = eliminado;
    }
}
