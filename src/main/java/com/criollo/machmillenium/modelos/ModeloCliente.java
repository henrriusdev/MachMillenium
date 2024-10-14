package com.criollo.machmillenium.modelos;

import com.criollo.machmillenium.entidades.Cliente;

public class ModeloCliente {
    private Long id;
    private String nombre;
    private String cedula;
    private String telefono;
    private String direccion;
    private Integer edad;
    private String correo;
    private String sexo;

    public ModeloCliente() {
    }

    public ModeloCliente(String nombre, String cedula, String telefono, String direccion, Integer edad, String correo, String sexo) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.telefono = telefono;
        this.direccion = direccion;
        this.edad = edad;
        this.correo = correo;
        this.sexo = sexo;
    }

    public ModeloCliente(Cliente cliente) {
        this.id = cliente.getId();
        this.nombre = cliente.getNombre();
        this.cedula = cliente.getCedula();
        this.telefono = cliente.getTelefono();
        this.direccion = cliente.getDireccion();
        this.edad = cliente.getEdad();
        this.correo = cliente.getCorreo();
        this.sexo = cliente.getSexo();
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

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
