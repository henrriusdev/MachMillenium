package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Personal;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class PersonalRepo {
    private Session sesion;

    public PersonalRepo() {
        this.sesion = HibernateUtil.getSession();
    }

    public List<Personal> obtenerTodos() {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Personal> criteriaQuery = criteriaBuilder.createQuery(Personal.class);
        criteriaQuery.from(Personal.class);
        Query<Personal> query = sesion.createQuery(criteriaQuery);
        List<Personal> personalList = query.getResultList();
        sesion.getTransaction().commit();
        return personalList;
    }

    public void insertar(Personal personal) {
        sesion.beginTransaction();
        sesion.persist(personal);
        sesion.getTransaction().commit();
    }

    public void actualizar(Personal personal) {
        sesion.beginTransaction();
        sesion.merge(personal);
        sesion.getTransaction().commit();
    }

    public void eliminar(Personal personal) {
        sesion.beginTransaction();
        sesion.remove(personal);
        sesion.getTransaction().commit();
    }

    public Long contar() {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Personal.class)));
        Query<Long> query = sesion.createQuery(criteriaQuery);
        Long count = query.getSingleResult();
        sesion.getTransaction().commit();
        return count;
    }
}

