package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;
import java.util.Set;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "personal")
public class Personal {
    public Personal() {
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
    }

    public Personal(String nombre, String cedula, String correo, Boolean fijo, LocalDateTime fechaTerminoContrato, Rol rol, Especialidad especialidad) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.correo = correo;
        this.fijo = fijo;
        this.fechaTerminoContrato = fechaTerminoContrato;
        this.rol = rol;
        this.especialidad = especialidad;
        this.activo = true;
        this.creado = LocalDateTime.now();
        this.modificado = LocalDateTime.now();
        this.clave = BCrypt.hashpw("12345678", BCrypt.gensalt());
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaTerminoContrato() {
        return fechaTerminoContrato;
    }

    public void setFechaTerminoContrato(LocalDateTime fechaTerminoContrato) {
        this.fechaTerminoContrato = fechaTerminoContrato;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String cedula;
    private String correo;
    private String clave;

    private Boolean fijo;
    private Boolean activo;

    @Column(name = "fecha_termino_contrato")
    private LocalDateTime fechaTerminoContrato;

    @ManyToOne
    @JoinColumn(name = "especialidad_id")
    private Especialidad especialidad;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    private LocalDateTime creado;
    private LocalDateTime modificado;
    private LocalDateTime eliminado;

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
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

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public Boolean getFijo() {
        return fijo;
    }

    public void setFijo(Boolean fijo) {
        this.fijo = fijo;
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

    @OneToMany(mappedBy = "personal", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PersonalPrivilegio> personalPrivilegios = new HashSet<>();

    public Set<Privilegio> getPrivilegios() {
        return personalPrivilegios.stream()
            .filter(PersonalPrivilegio::isActivo)
            .map(PersonalPrivilegio::getPrivilegio)
            .collect(Collectors.toSet());
    }

    public void setPrivilegios(Set<Privilegio> privilegios) {
        // Desactivar todos los privilegios existentes
        personalPrivilegios.forEach(pp -> pp.setActivo(false));
        
        // Agregar o reactivar los nuevos privilegios
        privilegios.forEach(privilegio -> {
            PersonalPrivilegio existente = personalPrivilegios.stream()
                .filter(pp -> pp.getPrivilegio().equals(privilegio))
                .findFirst()
                .orElse(null);
                
            if (existente != null) {
                existente.setActivo(true);
            } else {
                PersonalPrivilegio nuevo = new PersonalPrivilegio(this, privilegio);
                personalPrivilegios.add(nuevo);
            }
        });
    }

    public void agregarPrivilegio(Privilegio privilegio) {
        PersonalPrivilegio existente = personalPrivilegios.stream()
            .filter(pp -> pp.getPrivilegio().equals(privilegio))
            .findFirst()
            .orElse(null);
            
        if (existente != null) {
            existente.setActivo(true);
        } else {
            PersonalPrivilegio nuevo = new PersonalPrivilegio(this, privilegio);
            personalPrivilegios.add(nuevo);
        }
    }

    public void removerPrivilegio(Privilegio privilegio) {
        personalPrivilegios.stream()
            .filter(pp -> pp.getPrivilegio().equals(privilegio))
            .findFirst()
            .ifPresent(pp -> pp.setActivo(false));
    }
}
