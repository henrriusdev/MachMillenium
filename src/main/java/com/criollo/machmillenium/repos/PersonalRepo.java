package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Especialidad;
import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.entidades.Rol;
import com.criollo.machmillenium.modelos.ModeloPersonal;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class PersonalRepo {
    private final Session sesion;

    public PersonalRepo() {
        this.sesion = HibernateUtil.getSession();
    }

    public List<ModeloPersonal> obtenerTodos() {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Personal> criteriaQuery = criteriaBuilder.createQuery(Personal.class);
        criteriaQuery.from(Personal.class);
        Query<Personal> query = sesion.createQuery(criteriaQuery);
        List<Personal> personalList = query.getResultList();
        sesion.getTransaction().commit();

        List<ModeloPersonal> modeloPersonalList = new ArrayList<>();
        for (Personal personal : personalList) {
            modeloPersonalList.add(new ModeloPersonal(personal));
        }

        return modeloPersonalList;
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

    public Personal obtenerPorCorreo(String correo) {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Personal> criteriaQuery = criteriaBuilder.createQuery(Personal.class);
        criteriaQuery.where(criteriaBuilder.equal(criteriaQuery.from(Personal.class).get("correo"), correo));
        Query<Personal> query = sesion.createQuery(criteriaQuery);
        Personal personal = query.getSingleResult();
        sesion.getTransaction().commit();
        return personal;
    }

    public Rol obtenerRolPorNombre(String nombre) {
        sesion.beginTransaction();
        Rol rol = sesion.bySimpleNaturalId(Rol.class).load(nombre);
        sesion.getTransaction().commit();
        return rol;
    }

    public Especialidad obtenerOCrearEspecialidad(String nombre) {
        sesion.beginTransaction();
        Especialidad especialidad = sesion.bySimpleNaturalId(Especialidad.class).load(nombre);
        if (especialidad == null) {
            especialidad = new Especialidad();
            especialidad.setNombre(nombre);
            sesion.persist(especialidad);
        }
        sesion.getTransaction().commit();
        return especialidad;
    }
}

