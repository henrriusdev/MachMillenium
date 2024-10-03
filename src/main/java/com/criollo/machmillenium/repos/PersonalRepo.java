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
    private Session session;

    public PersonalRepo() {
        this.session = HibernateUtil.getSession();
    }

    public List<Personal> obtenerTodos() {
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Personal> criteriaQuery = criteriaBuilder.createQuery(Personal.class);
        criteriaQuery.from(Personal.class);
        Query<Personal> query = session.createQuery(criteriaQuery);
        List<Personal> personalList = query.getResultList();
        session.getTransaction().commit();
        return personalList;
    }

    public void insertar(Personal personal) {
        session.beginTransaction();
        session.persist(personal);
        session.getTransaction().commit();
    }

    public void actualizar(Personal personal) {
        session.beginTransaction();
        session.merge(personal);
        session.getTransaction().commit();
    }

    public void eliminar(Personal personal) {
        session.beginTransaction();
        session.remove(personal);
        session.getTransaction().commit();
    }

    public Long contar() {
        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Personal.class)));
        Query<Long> query = session.createQuery(criteriaQuery);
        Long count = query.getSingleResult();
        session.getTransaction().commit();
        return count;
    }
}

