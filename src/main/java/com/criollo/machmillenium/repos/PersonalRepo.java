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
    private SessionFactory sessionFactory;

    public PersonalRepo() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public List<Personal> obtenerTodos() {
        Session session = sessionFactory.getCurrentSession();
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
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(personal);
        session.getTransaction().commit();
    }

    public void actualizar(Personal personal) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.merge(personal);
        session.getTransaction().commit();
    }

    public void eliminar(Personal personal) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.remove(personal);
        session.getTransaction().commit();
    }

    public Long contar() {
        Session session = sessionFactory.getCurrentSession();
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

