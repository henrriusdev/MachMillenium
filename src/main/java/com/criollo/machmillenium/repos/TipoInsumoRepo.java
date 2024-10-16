package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.TipoInsumo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.List;

public class TipoInsumoRepo {
    private final Session sesion;

    public TipoInsumoRepo() {
        this.sesion = HibernateUtil.getSession();
    }

    public void insertar(TipoInsumo tipoInsumo) {
        sesion.beginTransaction();
        sesion.persist(tipoInsumo);
        sesion.getTransaction().commit();
    }

    public void actualizar(TipoInsumo tipoInsumo) {
        sesion.beginTransaction();
        sesion.merge(tipoInsumo);
        sesion.getTransaction().commit();
    }

    public TipoInsumo obtenerPorNombre(String nombre) {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<TipoInsumo> criteriaQuery = criteriaBuilder.createQuery(TipoInsumo.class);
        criteriaQuery.select(criteriaQuery.from(TipoInsumo.class)).where(criteriaBuilder.and(criteriaBuilder.isNull(criteriaQuery.from(TipoInsumo.class).get("eliminado")), criteriaBuilder.equal(criteriaQuery.from(TipoInsumo.class).get("nombre"), nombre)));
        TipoInsumo tipoInsumo = sesion.createQuery(criteriaQuery).getSingleResult();
        sesion.getTransaction().commit();
        return tipoInsumo;
    }

    public void eliminar(Long id) {
        sesion.beginTransaction();
        TipoInsumo tipoInsumo = sesion.get(TipoInsumo.class, id);
        tipoInsumo.setEliminado(LocalDateTime.now());
        sesion.merge(tipoInsumo);
        sesion.getTransaction().commit();
    }

    public TipoInsumo obtenerPorId(Long id) {
        sesion.beginTransaction();
        TipoInsumo tipoInsumo = sesion.get(TipoInsumo.class, id);
        sesion.getTransaction().commit();
        return tipoInsumo;
    }

    public List<TipoInsumo> obtenerTodos() {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<TipoInsumo> criteriaQuery = criteriaBuilder.createQuery(TipoInsumo.class);
        criteriaQuery.select(criteriaQuery.from(TipoInsumo.class)).where(criteriaBuilder.isNull(criteriaQuery.from(TipoInsumo.class).get("eliminado")));
        List<TipoInsumo> tipoInsumoList = sesion.createQuery(criteriaQuery).getResultList();
        sesion.getTransaction().commit();
        return tipoInsumoList;
    }

}
