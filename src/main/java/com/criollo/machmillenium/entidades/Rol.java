package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDateTime;

@Entity
@Table(name = "roles")
public class Rol {
    public Rol (String nombre) {
        this.nombre = nombre;
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public Rol() {
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public Rol(String nombre, LocalDateTime creado, LocalDateTime modificado, LocalDateTime eliminado) {
        this.nombre = nombre;
        this.creado = creado;
        this.modificado = modificado;
        this.eliminado = eliminado;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private String nombre;

    private LocalDateTime creado;
    private LocalDateTime modificado;
    private LocalDateTime eliminado;

    public LocalDateTime getEliminado() {
        return eliminado;
    }

    public void setEliminado(LocalDateTime eliminado) {
        this.eliminado = eliminado;
    }

    public LocalDateTime getModificado() {
        return modificado;
    }

    public void setModificado(LocalDateTime modificado) {
        this.modificado = modificado;
    }

    public LocalDateTime getCreado() {
        return creado;
    }

    public void setCreado(LocalDateTime creado) {
        this.creado = creado;
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
}
