package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Presupuesto;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.List;

public class PresupuestoRepo {
    private final Session sesion;

    public PresupuestoRepo() {
        this.sesion = HibernateUtil.getSession();
    }

    public void insertar(Presupuesto presupuesto) {
        sesion.beginTransaction();
        sesion.persist(presupuesto);
        sesion.getTransaction().commit();
    }

    public void actualizar(Presupuesto presupuesto) {
        sesion.beginTransaction();
        sesion.merge(presupuesto);
        sesion.getTransaction().commit();
    }

    public Presupuesto obtenerPorId(Long id) {
        sesion.beginTransaction();
        Presupuesto presupuesto = sesion.get(Presupuesto.class, id);
        sesion.getTransaction().commit();

        return presupuesto;
    }

    public void eliminar(Long id) {
        sesion.beginTransaction();
        Presupuesto presupuesto = sesion.get(Presupuesto.class, id);
        presupuesto.setEliminado(LocalDateTime.now());
        sesion.merge(presupuesto);
        sesion.getTransaction().commit();
    }

    public List<Presupuesto> obtenerTodos() {
        sesion.beginTransaction();
        List<Presupuesto> presupuestos = sesion.createQuery("from Presupuesto", Presupuesto.class).list();
        sesion.getTransaction().commit();
        return presupuestos;
    }
}
