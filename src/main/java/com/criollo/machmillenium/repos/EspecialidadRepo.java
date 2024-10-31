package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Especialidad;
import org.hibernate.Session;

public class EspecialidadRepo {
    private final Session sesion;

    public EspecialidadRepo() {
        this.sesion = HibernateUtil.getSession();
    }

    public Especialidad insertar(Especialidad especialidad) {
        sesion.beginTransaction();
        sesion.persist(especialidad);
        sesion.getTransaction().commit();
        sesion.refresh(especialidad);
        return especialidad;
    }

    public void actualizar(Especialidad especialidad) {
        sesion.beginTransaction();
        sesion.merge(especialidad);
        sesion.getTransaction().commit();
    }

    public Especialidad obtenerPorNombre(String nombre) {
        sesion.beginTransaction();
        Especialidad especialidad = sesion.byNaturalId(Especialidad.class).using("nombre", nombre).load();
        sesion.getTransaction().commit();
        return especialidad;
    }

    public Especialidad encuentraOInserta(String nombre) {
        Especialidad especialidad = obtenerPorNombre(nombre);
        if (especialidad == null) {
            especialidad = new Especialidad();
            especialidad.setNombre(nombre);
            especialidad = insertar(especialidad);
        }
        return especialidad;
    }
}
