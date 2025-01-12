package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "privilegios")
public class Privilegio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column
    private String descripcion;

    @ManyToMany(mappedBy = "privilegios")
    private Set<Personal> personal;

    // Getters y Setters
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Personal> getPersonal() {
        return personal;
    }

    public void setPersonal(Set<Personal> personal) {
        this.personal = personal;
    }
}
