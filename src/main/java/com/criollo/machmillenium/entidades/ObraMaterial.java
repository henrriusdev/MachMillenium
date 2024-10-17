package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "obras_materiales")
public class ObraMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "obra_id")
    private Obra obra;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    private LocalDateTime creado;
    private LocalDateTime modificado;
    private LocalDateTime eliminado;

    public ObraMaterial() {
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public ObraMaterial(Obra obra, Material material) {
        this.obra = obra;
        this.material = material;
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

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
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
