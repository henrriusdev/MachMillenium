package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Auditoria;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.Session;

import java.util.List;

public class AuditoriaRepo {
    private String personal;
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
        CriteriaBuilder builder = sesion.getCriteriaBuilder();
        return sesion.createQuery(builder.createQuery(Auditoria.class)).getResultList();
    }
}
