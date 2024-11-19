package com.criollo.machmillenium.modelos;

public class ModeloSolicitudCompra {
    private String fecha;
    private String solicitante;
    private String cargo;
    private String nombreMaterial;
    private String tipoMaterial;
    private String cantidad;
    private String presentacion;
    private String justificacion;
    private String fechaLimite;

    public ModeloSolicitudCompra() {
    }

    public ModeloSolicitudCompra(String fecha, String solicitante, String cargo, String nombreMaterial, String tipoMaterial, String cantidad, String presentacion, String justificacion, String fechaLimite) {
        this.fecha = fecha;
        this.solicitante = solicitante;
        this.cargo = cargo;
        this.nombreMaterial = nombreMaterial;
        this.tipoMaterial = tipoMaterial;
        this.cantidad = cantidad;
        this.presentacion = presentacion;
        this.justificacion = justificacion;
        this.fechaLimite = fechaLimite;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getNombreMaterial() {
        return nombreMaterial;
    }

    public void setNombreMaterial(String nombreMaterial) {
        this.nombreMaterial = nombreMaterial;
    }

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public void setTipoMaterial(String tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public String getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(String fechaLimite) {
        this.fechaLimite = fechaLimite;
    }
}
