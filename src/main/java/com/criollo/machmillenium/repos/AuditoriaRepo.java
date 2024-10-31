package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Auditoria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditoriaRepo {
    private final String personal;
    private final Session sesion;

    public AuditoriaRepo(String personal) {
        this.personal = personal;
        this.sesion = HibernateUtil.getSession();
    }

    public void registrar(String accion, String detalle) {
        Auditoria auditoria = new Auditoria(personal);
        auditoria.registrar(accion, detalle);
        sesion.beginTransaction();
        sesion.persist(auditoria);
        sesion.getTransaction().commit();
    }

    public List<Auditoria> obtenerAuditorias() {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Auditoria> criteriaQuery = criteriaBuilder.createQuery(Auditoria.class);
        Root<Auditoria> root = criteriaQuery.from(Auditoria.class); // Indica la clase raíz
        criteriaQuery.select(root);
        Query<Auditoria> query = sesion.createQuery(criteriaQuery);
        List<Auditoria> personalList = query.getResultList();
        sesion.getTransaction().commit();
        return personalList;
    }

    public List<Auditoria> obtenerAuditoriasPorFiltros(String personal, LocalDate realizado, String accion, String detalle) {
        CriteriaBuilder builder = sesion.getCriteriaBuilder();
        CriteriaQuery<Auditoria> query = builder.createQuery(Auditoria.class);
        Root<Auditoria> root = query.from(Auditoria.class);

        List<Predicate> predicates = new ArrayList<>();

        if (personal != null && !personal.isEmpty()) {
            predicates.add(builder.equal(root.get("personal"), personal));
        }
        if (realizado != null) {
            LocalDateTime startOfDay = realizado.atStartOfDay();
            LocalDateTime endOfDay = realizado.atTime(23, 59, 59);
            predicates.add(builder.between(root.get("realizado"), startOfDay, endOfDay));
        }
        if (accion != null && !accion.isEmpty()) {
            predicates.add(builder.equal(root.get("accion"), accion));
        }
        if (detalle != null && !detalle.isEmpty()) {
            predicates.add(builder.equal(root.get("detalle"), detalle));
        }

        query.select(root).where(predicates.toArray(new Predicate[0]));
        return sesion.createQuery(query).getResultList();
    }

}
