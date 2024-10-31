package com.criollo.machmillenium.modelos;

import java.time.Duration;

public class ModeloMaquinaria {
    private Long id;
    private Long tipoMaquinariaId;
    private String tipoMaquinaria;
    private String nombre;
    private String tiempoEstimadoDeUso;
    private Double costoPorTiempoDeUso;
    private Double costoTotal;

    public ModeloMaquinaria() {
    }

    public ModeloMaquinaria(Long id, Long tipoMaquinariaId, String nombre, Duration tiempoEstimadoDeUso, Double costoPorTiempoDeUso, Double costoTotal, String tipoMaquinaria) {
        this.id = id;
        this.tipoMaquinariaId = tipoMaquinariaId;
        this.nombre = nombre;
        this.tipoMaquinaria = tipoMaquinaria;

        long horas = tiempoEstimadoDeUso.toHours();
        long minutos = tiempoEstimadoDeUso.minusHours(horas).toMinutes();
        this.tiempoEstimadoDeUso = String.format("%02d horas con %02d minutos", horas, minutos);
        this.costoPorTiempoDeUso = costoPorTiempoDeUso;
        this.costoTotal = costoTotal;
    }

    public Long getId() {
        return id;
    }

    public Long getTipoMaquinariaId() {
        return tipoMaquinariaId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTiempoEstimadoDeUso() {
        return tiempoEstimadoDeUso;
    }

    public Double getCostoPorTiempoDeUso() {
        return costoPorTiempoDeUso;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTipoMaquinariaId(Long tipoMaquinariaId) {
        this.tipoMaquinariaId = tipoMaquinariaId;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTiempoEstimadoDeUso(String tiempoEstimadoDeUso) {
        this.tiempoEstimadoDeUso = tiempoEstimadoDeUso;
    }

    public void setCostoPorTiempoDeUso(Double costoPorTiempoDeUso) {
        this.costoPorTiempoDeUso = costoPorTiempoDeUso;
    }

    public Double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(Double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public String getTipoMaquinaria() {
        return tipoMaquinaria;
    }

    public void setTipoMaquinaria(String tipoMaquinaria) {
        this.tipoMaquinaria = tipoMaquinaria;
    }
}
