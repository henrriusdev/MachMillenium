package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tipo_maquinaria")
public class TipoMaquinaria {
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private LocalDateTime creado;
    private LocalDateTime modificado;
    private LocalDateTime eliminado;

    public TipoMaquinaria() {
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public TipoMaquinaria(String nombre) {
        this.nombre = nombre;
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public TipoMaquinaria(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }
}
