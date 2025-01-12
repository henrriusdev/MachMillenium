package com.criollo.machmillenium.entidades;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "roles_privilegios")
public class RolPrivilegio implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;
    
    @ManyToOne
    @JoinColumn(name = "privilegio_id", nullable = false)
    private Privilegio privilegio;
    
    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Privilegio getPrivilegio() {
        return privilegio;
    }

    public void setPrivilegio(Privilegio privilegio) {
        this.privilegio = privilegio;
    }
}
