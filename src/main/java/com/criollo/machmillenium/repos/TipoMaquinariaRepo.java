package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.TipoMaquinaria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class TipoMaquinariaRepo {
    private final Session sesion;

    public TipoMaquinariaRepo() {
        this.sesion = HibernateUtil.getSessionFactory().openSession();
    }

    public List<TipoMaquinaria> obtenerTodos() {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<TipoMaquinaria> criteriaQuery = criteriaBuilder.createQuery(TipoMaquinaria.class);
        criteriaQuery.from(TipoMaquinaria.class);
        Query<TipoMaquinaria> query = sesion.createQuery(criteriaQuery);
        List<TipoMaquinaria> tipoMaquinariaList = query.getResultList();
        sesion.getTransaction().commit();
        return tipoMaquinariaList;
    }

    public void insertar(TipoMaquinaria tipoMaquinaria) {
        sesion.beginTransaction();
        sesion.persist(tipoMaquinaria);
        sesion.getTransaction().commit();
    }

    public void actualizar(TipoMaquinaria tipoMaquinaria) {
        sesion.beginTransaction();
        sesion.merge(tipoMaquinaria);
        sesion.getTransaction().commit();
    }

    public void eliminar(TipoMaquinaria tipoMaquinaria) {
        sesion.beginTransaction();
        sesion.remove(tipoMaquinaria);
        sesion.getTransaction().commit();
    }
}
