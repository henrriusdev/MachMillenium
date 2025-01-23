package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Especialidad;
import org.hibernate.Session;
import java.util.List;

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
        try {
            sesion.beginTransaction();
            sesion.merge(especialidad);
            sesion.getTransaction().commit();
        } catch (Exception e) {
            sesion.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    public Especialidad obtenerPorNombre(String nombre) {
        sesion.beginTransaction();
        Especialidad especialidad = sesion.createQuery("FROM Especialidad e WHERE e.nombre = :nombre AND e.eliminado IS NULL", Especialidad.class)
                .setParameter("nombre", nombre)
                .uniqueResult();
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

    public List<Especialidad> obtenerTodos() {
        sesion.beginTransaction();
        List<Especialidad> especialidades = sesion.createQuery("FROM Especialidad e WHERE e.eliminado IS NULL", Especialidad.class)
                .getResultList();
        sesion.getTransaction().commit();
        return especialidades;
    }

    public boolean existePorNombre(String nombre) {
        sesion.beginTransaction();
        Long cantidad = (Long) sesion.createQuery("SELECT COUNT(e) FROM Especialidad e WHERE e.nombre = :nombre AND e.eliminado IS NULL")
                .setParameter("nombre", nombre)
                .uniqueResult();
        sesion.getTransaction().commit();
        return cantidad > 0;
    }

    public boolean estaEnUso(Long id) {
        sesion.beginTransaction();
        Long cantidad = (Long) sesion.createQuery("SELECT COUNT(m) FROM Personal p JOIN p.especialidades m WHERE m.id = :id")
                .setParameter("id", id)
                .uniqueResult();
        sesion.getTransaction().commit();
        return cantidad > 0;
    }
}
