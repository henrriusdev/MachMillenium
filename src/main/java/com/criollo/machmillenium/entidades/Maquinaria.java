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
    private Double costoTotal;

    private LocalDateTime creado;
    private LocalDateTime modificado;
    private LocalDateTime eliminado;

    public Maquinaria() {
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoMaquinaria getTipoMaquinaria() {
        return tipoMaquinaria;
    }

    public void setTipoMaquinaria(TipoMaquinaria tipoMaquinaria) {
        this.tipoMaquinaria = tipoMaquinaria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Duration getTiempoEstimadoDeUso() {
        return tiempoEstimadoDeUso;
    }

    public void setTiempoEstimadoDeUso(Duration tiempoEstimadoDeUso) {
        this.tiempoEstimadoDeUso = tiempoEstimadoDeUso;
    }

    public Double getCostoPorTiempoDeUso() {
        return costoPorTiempoDeUso;
    }

    public void setCostoPorTiempoDeUso(Double costoPorTiempoDeUso) {
        this.costoPorTiempoDeUso = costoPorTiempoDeUso;
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

    public Double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(Double costoTotal) {
        this.costoTotal = costoTotal;
    }
}
