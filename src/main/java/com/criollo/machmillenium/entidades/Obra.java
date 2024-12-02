package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.Duration;

@Entity
@Table(name = "obras")
public class Obra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tipo_de_obra_id")
    private TipoObra tipoObra;

    private Double area;
    private String nombre;
    private String descripcion;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "presupuesto_id")
    private Presupuesto presupuesto;

    private LocalDateTime creado;
    private LocalDateTime modificado;
    private LocalDateTime eliminado;

    @Column(name = "tiempo_total")
    private Duration tiempoTotal;

    public Obra() {
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public Obra(TipoObra tipoObra, Double area, String nombre, String estado, String descripcion, Presupuesto presupuesto) {
        this.tipoObra = tipoObra;
        this.area = area;
        this.nombre = nombre;
        this.estado = estado;
        this.descripcion = descripcion;
        this.presupuesto = presupuesto;
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public Obra(Long id, TipoObra tipoObra, Double area, String nombre, String estado, String descripcion, Presupuesto presupuesto) {
        this.id = id;
        this.tipoObra = tipoObra;
        this.area = area;
        this.nombre = nombre;
        this.estado = estado;
        this.descripcion = descripcion;
        this.presupuesto = presupuesto;
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoObra getTipoObra() {
        return tipoObra;
    }

    public void setTipoObra(TipoObra tipoObra) {
        this.tipoObra = tipoObra;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Presupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Presupuesto presupuesto) {
        this.presupuesto = presupuesto;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Duration getTiempoTotal() {
        return tiempoTotal;
    }

    public void setTiempoTotal(Duration tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }
}
