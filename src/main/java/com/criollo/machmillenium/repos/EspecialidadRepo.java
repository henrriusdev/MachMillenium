package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Especialidad;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class EspecialidadRepo {
    private Session session;

    public EspecialidadRepo() {
        this.session = HibernateUtil.getSession();
    }

    public Especialidad insertar(Especialidad especialidad) {
        session.beginTransaction();
        session.persist(especialidad);
        session.getTransaction().commit();
        session.refresh(especialidad);
        return especialidad;
    }

    public void actualizar(Especialidad especialidad) {
        session.beginTransaction();
        session.merge(especialidad);
        session.getTransaction().commit();
    }

    public void eliminar(Especialidad especialidad) {
        session.beginTransaction();
        session.remove(especialidad);
        session.getTransaction().commit();
    }

    public Especialidad obtenerPorNombre(String nombre) {
        session.beginTransaction();
        Especialidad especialidad = session.byNaturalId(Especialidad.class).using("nombre", nombre).load();
        session.getTransaction().commit();
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
