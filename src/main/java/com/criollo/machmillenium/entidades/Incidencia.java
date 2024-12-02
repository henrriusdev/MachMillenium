package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.time.Duration;

@Entity
@Table(name = "incidencias")
public class Incidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoIncidencia tipo;

    private String descripcion;
    private Duration retraso;
    private LocalDateTime fechaIncidencia;
    private LocalDateTime creado;
    private LocalDateTime modificado;

    @ManyToOne
    @JoinColumn(name = "obra_id")
    private Obra obra;

    public enum TipoIncidencia {
        CONDICION_AMBIENTAL("Condición Ambiental"),
        ACCIDENTE("Accidente"),
        RETRASO("Retraso"),
        COMPLICACION("Complicación"),
        INASISTENCIA("Inasistencia de personal");

        private final String nombre;

        TipoIncidencia(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    public Incidencia() {
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
        this.fechaIncidencia = LocalDateTime.now();
    }

    public Incidencia(TipoIncidencia tipo, String descripcion, Duration retraso, Obra obra) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.retraso = retraso;
        this.obra = obra;
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
        this.fechaIncidencia = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoIncidencia getTipo() {
        return tipo;
    }

    public void setTipo(TipoIncidencia tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Duration getRetraso() {
        return retraso;
    }

    public void setRetraso(Duration retraso) {
        this.retraso = retraso;
    }

    public LocalDateTime getFechaIncidencia() {
        return fechaIncidencia;
    }

    public void setFechaIncidencia(LocalDateTime fechaIncidencia) {
        this.fechaIncidencia = fechaIncidencia;
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

    public Obra getObra() {
        return obra;
    }

    public void setObra(Obra obra) {
        this.obra = obra;
    }
}
