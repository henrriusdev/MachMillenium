package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "preguntas_seguridad")
public class PreguntasSeguridad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "personal_id")
    private Personal personal;

    @Column(name = "respuesta1", nullable = false)
    private String respuesta1;

    @Column(name = "respuesta2", nullable = false)
    private String respuesta2;

    @Column(name = "respuesta3", nullable = false)
    private String respuesta3;

    @Column(name = "creado")
    private LocalDateTime creado;

    @Column(name = "modificado")
    private LocalDateTime modificado;

    @Column(name = "eliminado")
    private LocalDateTime eliminado;

    public PreguntasSeguridad() {
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    public String getRespuesta1() {
        return respuesta1;
    }

    public void setRespuesta1(String respuesta1) {
        this.respuesta1 = respuesta1;
    }

    public String getRespuesta2() {
        return respuesta2;
    }

    public void setRespuesta2(String respuesta2) {
        this.respuesta2 = respuesta2;
    }

    public String getRespuesta3() {
        return respuesta3;
    }

    public void setRespuesta3(String respuesta3) {
        this.respuesta3 = respuesta3;
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
