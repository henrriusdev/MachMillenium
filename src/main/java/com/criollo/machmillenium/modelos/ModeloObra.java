package com.criollo.machmillenium.modelos;

import com.criollo.machmillenium.entidades.Obra;

public class ModeloObra {
    private Long id;
    private String nombre;
    private String descripcion;
    private String estado;
    private String clienteNombre;
    private String tipoObraNombre;
    private Double presupuestoTotal;

    public ModeloObra(Obra obra) {
        this.id = obra.getId();
        this.nombre = obra.getNombre();
        this.descripcion = obra.getDescripcion();
        this.estado = obra.getEstado();
        this.clienteNombre = obra.getPresupuesto().getCliente().getNombre();
        this.tipoObraNombre = obra.getTipoObra().getNombre();
        this.presupuestoTotal = obra.getPresupuesto().getCosto();
    }

    public ModeloObra(Long id, String nombre, String descripcion, String estado, 
                     String clienteNombre, String tipoObraNombre, Double presupuestoTotal) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.clienteNombre = clienteNombre;
        this.tipoObraNombre = tipoObraNombre;
        this.presupuestoTotal = presupuestoTotal;
    }

    public ModeloObra() {

    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getClienteNombre() { return clienteNombre; }
    public void setClienteNombre(String clienteNombre) { this.clienteNombre = clienteNombre; }

    public String getTipoObraNombre() { return tipoObraNombre; }
    public void setTipoObraNombre(String tipoObraNombre) { this.tipoObraNombre = tipoObraNombre; }

    public Double getPresupuestoTotal() { return presupuestoTotal; }
    public void setPresupuestoTotal(Double presupuestoTotal) { this.presupuestoTotal = presupuestoTotal; }
}
