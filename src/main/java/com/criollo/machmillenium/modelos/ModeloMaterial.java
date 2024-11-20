package com.criollo.machmillenium.modelos;

import com.criollo.machmillenium.entidades.Material;

public class ModeloMaterial {
    private Long id;
    private String nombre;
    private Long cantidad;
    private Double costo;
    private String tipoInsumoNombre;

    public ModeloMaterial(Material material) {
        this.id = material.getId();
        this.nombre = material.getNombre();
        this.cantidad = material.getCantidad();
        this.costo = material.getCosto();
        this.tipoInsumoNombre = material.getTipoInsumo().getNombre();
    }

    public ModeloMaterial(Long id, String nombre, Long cantidad, Double costo, String tipoInsumoNombre) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.costo = costo;
        this.tipoInsumoNombre = tipoInsumoNombre;
    }

    public ModeloMaterial() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getTipoInsumoNombre() {
        return tipoInsumoNombre;
    }

    public void setTipoInsumoNombre(String tipoInsumoNombre) {
        this.tipoInsumoNombre = tipoInsumoNombre;
    }
}
