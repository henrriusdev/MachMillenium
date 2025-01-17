package com.criollo.machmillenium.modelos;

import com.criollo.machmillenium.entidades.Personal;

import java.time.format.DateTimeFormatter;

public class ModeloPersonal {
    private Long id;

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

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFijo() {
        return fijo;
    }

    public void setFijo(String fijo) {
        this.fijo = fijo;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public String getFechaFinContrato() {
        return fechaFinContrato;
    }

    public void setFechaFinContrato(String fechaFinContrato) {
        this.fechaFinContrato = fechaFinContrato;
    }

    private String nombre;
    private String cedula;
    private String correo;
    private String fijo;
    private String especialidad;
    private String rol;
    private String activo;
    private String fechaFinContrato;

    public ModeloPersonal() {}

    public ModeloPersonal(Personal personal) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.id = personal.getId();
        this.nombre = personal.getNombre();
        this.cedula = personal.getCedula();
        this.correo = personal.getCorreo();
        this.fijo = personal.getFijo() ? "Sí" : "No";
        this.especialidad = personal.getEspecialidad().getNombre();
        this.rol = personal.getRol().getNombre();
        this.activo = personal.getActivo() != null && personal.getActivo() ? "Sí" : "No";
        this.fechaFinContrato = personal.getFechaTerminoContrato() != null ? personal.getFechaTerminoContrato().format(formatter) : "";
    }

    public ModeloPersonal(Long id, String nombre, String cedula, String correo, Boolean fijo, String especialidad, String rol, Boolean activo, String fechaFinContrato) {
        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
        this.correo = correo;
        this.fijo = fijo ? "Sí" : "No";
        this.especialidad = especialidad;
        this.rol = rol;
        this.activo = activo ? "Sí" : "No";
        this.fechaFinContrato = fechaFinContrato;
    }

}
