package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Rol;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class RolRepo {
    private Session sesion;

    public RolRepo() {
        this.sesion = HibernateUtil.getSession();
    }

    public void insertar(Rol rol) {
        sesion.beginTransaction();
        sesion.persist(rol);
        sesion.getTransaction().commit();
    }

    public void actualizar(Rol rol) {
        sesion.beginTransaction();
        sesion.merge(rol);
        sesion.getTransaction().commit();
    }

    public void eliminar(Rol rol) {
        sesion.beginTransaction();
        sesion.remove(rol);
        sesion.getTransaction().commit();
    }

    public Rol obtenerPorId(Long id) {
        sesion.beginTransaction();
        Rol rol = sesion.get(Rol.class, id);
        sesion.getTransaction().commit();
        return rol;
    }

    public Rol obtenerPorNombre(String nombre) {
        sesion.beginTransaction();
        Rol rol = sesion.bySimpleNaturalId(Rol.class).load(nombre);
        sesion.getTransaction().commit();
        return rol;
    }

    public Long contar() {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Rol.class)));
        Query<Long> query = sesion.createQuery(criteriaQuery);
        Long count = query.getSingleResult();
        sesion.getTransaction().commit();
        return count;
    }

    public List<Rol> obtenerTodos() {
        sesion.beginTransaction();
        CriteriaQuery<Rol> criteriaQuery = sesion.getCriteriaBuilder().createQuery(Rol.class);
        criteriaQuery.from(Rol.class);
        List<Rol> roles = sesion.createQuery(criteriaQuery).getResultList();
        sesion.getTransaction().commit();
        return roles;
    }
}
