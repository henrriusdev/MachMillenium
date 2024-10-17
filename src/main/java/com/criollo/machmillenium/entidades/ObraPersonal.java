package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "obras_personal")
public class ObraPersonal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "obra_id")
    private Obra obra;

    @ManyToOne
    @JoinColumn(name = "personal_id")
    private Personal personal;

    private LocalDateTime creado;
    private LocalDateTime modificado;
    private LocalDateTime eliminado;

    public ObraPersonal() {
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public ObraPersonal(Obra obra, Personal personal) {
        this.obra = obra;
        this.personal = personal;
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Obra getObra() {
        return obra;
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
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
}

