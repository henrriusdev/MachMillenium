package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "materiales")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tipo_insumo_id")
    private TipoInsumo tipoInsumo;

    private String nombre;

    private Long cantidad;
    private Double costo;

    private LocalDateTime creado;
    private LocalDateTime modificado;
    private LocalDateTime eliminado;

    public Material() {
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public Material(TipoInsumo tipoInsumo, Long cantidad, Double costo, String nombre) {
        this.tipoInsumo = tipoInsumo;
        this.cantidad = cantidad;
        this.costo = costo;
        this.nombre = nombre;
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public Material(Long id, String tipoInsumo, String nombre, Long cantidad, Double costo) {
        this.id = id;
        this.tipoInsumo = new TipoInsumo(tipoInsumo);
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.costo = costo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoInsumo getTipoInsumo() {
        return tipoInsumo;
    }

    public void setTipoInsumo(TipoInsumo tipoInsumo) {
        this.tipoInsumo = tipoInsumo;
    }

    public Long getCantidad() {
        return cantidad;
    }

    public void setCantidad(Long cantidad) {
        this.cantidad = cantidad;
    }

    public Double getCosto() {
        return costo;
    }

    public void setCosto(Double costo) {
        this.costo = costo;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
