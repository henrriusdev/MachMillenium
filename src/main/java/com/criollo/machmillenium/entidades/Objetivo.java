package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "objetivos")
public class Objetivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String objetivo;
    private boolean completado;
    private LocalDateTime creado;
    private LocalDateTime modificado;
    @Column(name = "completado_el")
    private LocalDateTime completadoEl;

    @ManyToOne
    @JoinColumn(name = "obra_id")
    private Obra obra;

    public Objetivo() {
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public Objetivo(String objetivo, Obra obra) {
        this.objetivo = objetivo;
        this.obra = obra;
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public boolean isCompletado() {
        return completado;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
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

    public LocalDateTime getCompletadoEl() {
        return completadoEl;
    }

    public void setCompletadoEl(LocalDateTime completadoEl) {
        this.completadoEl = completadoEl;
    }

    public Obra getObra() {
        return obra;
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }
}
