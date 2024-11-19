package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Maquinaria;
import com.criollo.machmillenium.entidades.TipoMaquinaria;
import com.criollo.machmillenium.modelos.ModeloMaquinaria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        Root<TipoMaquinaria> root = criteriaQuery.from(TipoMaquinaria.class);
        criteriaQuery.select(root).where(criteriaBuilder.isNull(root.get("eliminado")));
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

    public void eliminar(Long tipoMaquinaria) {
        sesion.beginTransaction();
        sesion.remove(tipoMaquinaria);
        sesion.getTransaction().commit();
    }

    public TipoMaquinaria obtenerPorNombre(String nombre) {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<TipoMaquinaria> criteriaQuery = criteriaBuilder.createQuery(TipoMaquinaria.class);
        Root<TipoMaquinaria> root = criteriaQuery.from(TipoMaquinaria.class);
        criteriaQuery.select(root).where(criteriaBuilder.and(criteriaBuilder.isNull(root.get("eliminado")), criteriaBuilder.equal(root.get("nombre"), nombre)));
        Query<TipoMaquinaria> query = sesion.createQuery(criteriaQuery);
        TipoMaquinaria tipoMaquinaria = query.getSingleResult();
        sesion.getTransaction().commit();
        return tipoMaquinaria;
    }

    public TipoMaquinaria obtenerPorId(Long id) {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<TipoMaquinaria> criteriaQuery = criteriaBuilder.createQuery(TipoMaquinaria.class);
        Root<TipoMaquinaria> root = criteriaQuery.from(TipoMaquinaria.class);
        criteriaQuery.select(root).where(criteriaBuilder.and(criteriaBuilder.isNull(root.get("eliminado")), criteriaBuilder.equal(root.get("id"), id)));
        Query<TipoMaquinaria> query = sesion.createQuery(criteriaQuery);
        TipoMaquinaria tipoMaquinaria = query.getSingleResult();
        sesion.getTransaction().commit();
        return tipoMaquinaria;
    }

    public void insertarMaquinaria(Maquinaria maquinaria) {
        sesion.beginTransaction();
        sesion.persist(maquinaria);
        sesion.getTransaction().commit();
    }

    public List<ModeloMaquinaria> obtenerTodosMaquinaria() {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Maquinaria> criteriaQuery = criteriaBuilder.createQuery(Maquinaria.class);
        Root<Maquinaria> root = criteriaQuery.from(Maquinaria.class);
        criteriaQuery.select(root).where(criteriaBuilder.isNull(root.get("eliminado")));
        Query<Maquinaria> query = sesion.createQuery(criteriaQuery);
        List<Maquinaria> maquinariaList = query.getResultList();
        sesion.getTransaction().commit();

        List<ModeloMaquinaria> modeloMaquinariaList = new ArrayList<>();
        maquinariaList.forEach(maquinaria -> modeloMaquinariaList.add(new ModeloMaquinaria(maquinaria.getId(), maquinaria.getTipoMaquinaria().getId(), maquinaria.getNombre(), maquinaria.getTiempoEstimadoDeUso(), maquinaria.getCostoPorTiempoDeUso(), maquinaria.getCostoTotal(), maquinaria.getTipoMaquinaria().getNombre())));
        return modeloMaquinariaList;
    }

    public List<Maquinaria> obtenerMaquinarias() {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Maquinaria> criteriaQuery = criteriaBuilder.createQuery(Maquinaria.class);
        Root<Maquinaria> root = criteriaQuery.from(Maquinaria.class);
        criteriaQuery.select(root).where(criteriaBuilder.isNull(root.get("eliminado")));
        Query<Maquinaria> query = sesion.createQuery(criteriaQuery);
        List<Maquinaria> maquinariaList = query.getResultList();
        sesion.getTransaction().commit();
        return maquinariaList;
    }

    public Maquinaria obtenerMaquinariaPorId(Long id) {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Maquinaria> criteriaQuery = criteriaBuilder.createQuery(Maquinaria.class);
        Root<Maquinaria> root = criteriaQuery.from(Maquinaria.class);
        criteriaQuery.select(root).where(criteriaBuilder.and(criteriaBuilder.isNull(root.get("eliminado")), criteriaBuilder.equal(root.get("id"), id)));
        Query<Maquinaria> query = sesion.createQuery(criteriaQuery);
        Maquinaria maquinaria = query.getSingleResult();
        sesion.getTransaction().commit();
        return maquinaria;
    }

    public void actualizarMaquinaria(Maquinaria maquinaria) {
        sesion.beginTransaction();
        sesion.merge(maquinaria);
        sesion.getTransaction().commit();
    }

    public void eliminarMaquinaria(Long id) {
        Maquinaria maquinaria = obtenerMaquinariaPorId(id);
        maquinaria.setEliminado(LocalDateTime.now());
        sesion.beginTransaction();
        sesion.merge(maquinaria);
        sesion.getTransaction().commit();
    }

    public List<ModeloMaquinaria> obtenerMaquinariasFiltradas(String nombre, Double costoMin, Double costoMax, String tipo) {
        try (Session session = HibernateUtil.getSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Maquinaria> query = builder.createQuery(Maquinaria.class);
            Root<Maquinaria> root = query.from(Maquinaria.class);
            Join<Maquinaria, TipoMaquinaria> tipoJoin = root.join("tipoMaquinaria", JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();

            if (nombre != null && !nombre.isEmpty()) {
                predicates.add(builder.like(builder.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
            }

            if (costoMin != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("costoTotal"), costoMin));
            }

            if (costoMax != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("costoTotal"), costoMax));
            }

            if (tipo != null && !tipo.equals("Todos")) {
                predicates.add(builder.equal(tipoJoin.get("nombre"), tipo));
            }

            if (!predicates.isEmpty()) {
                query.where(predicates.toArray(new Predicate[0]));
            }

            List<Maquinaria> maquinariaList = session.createQuery(query).getResultList();
            List<ModeloMaquinaria> modeloMaquinariaList = new ArrayList<>();
            maquinariaList.forEach(maquinaria -> modeloMaquinariaList.add(
                    new ModeloMaquinaria(
                            maquinaria.getId(),
                            maquinaria.getTipoMaquinaria().getId(),
                            maquinaria.getNombre(), maquinaria.getTiempoEstimadoDeUso(),
                            maquinaria.getCostoPorTiempoDeUso(),
                            maquinaria.getCostoTotal(),
                            maquinaria.getTipoMaquinaria().getNombre())));
            return modeloMaquinariaList;
        }
    }
}
