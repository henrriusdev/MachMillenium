package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.entidades.PersonalPrivilegio;
import com.criollo.machmillenium.entidades.Privilegio;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PrivilegioRepo {
    private final Session sesion;

    public PrivilegioRepo() {
        sesion = HibernateUtil.getSessionFactory().openSession();
    }

    // Métodos CRUD básicos para Privilegio
    public void guardar(Privilegio privilegio) {
        Transaction tx = null;
        try {
            tx = sesion.beginTransaction();
            sesion.persist(privilegio);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void actualizar(Privilegio privilegio) {
        Transaction tx = null;
        try {
            tx = sesion.beginTransaction();
            sesion.merge(privilegio);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void eliminar(Privilegio privilegio) {
        Transaction tx = null;
        try {
            tx = sesion.beginTransaction();
            sesion.remove(privilegio);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public Privilegio obtenerPorId(Long id) {
        return sesion.get(Privilegio.class, id);
    }

    public List<Privilegio> obtenerTodos() {
        return sesion.createQuery("FROM Privilegio", Privilegio.class).list();
    }

    public Privilegio obtenerPorNombre(String nombre) {
        return sesion.createQuery(
            "FROM Privilegio WHERE nombre = :nombre", 
            Privilegio.class
        )
        .setParameter("nombre", nombre)
        .uniqueResult();
    }

    // Métodos para gestionar la relación Personal-Privilegio
    public void asignarPrivilegioAPersonal(Personal personal, Privilegio privilegio) {
        Transaction tx = null;
        try {
            tx = sesion.beginTransaction();
            PersonalPrivilegio pp = new PersonalPrivilegio(personal, privilegio);
            sesion.persist(pp);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public void revocarPrivilegioDePersonal(Personal personal, Privilegio privilegio) {
        Transaction tx = null;
        try {
            tx = sesion.beginTransaction();
            Query<?> query = sesion.createQuery(
                "UPDATE PersonalPrivilegio SET activo = false " +
                "WHERE personal = :personal AND privilegio = :privilegio AND activo = true"
            );
            query.setParameter("personal", personal);
            query.setParameter("privilegio", privilegio);
            query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    public Set<Privilegio> obtenerPrivilegiosDePersonal(Personal personal) {
        return sesion.createQuery(
            "SELECT pp.privilegio FROM PersonalPrivilegio pp " +
            "WHERE pp.personal = :personal AND pp.activo = true", 
            Privilegio.class
        )
        .setParameter("personal", personal)
        .stream()
        .collect(Collectors.toSet());
    }

    public List<Personal> obtenerPersonalConPrivilegio(Privilegio privilegio) {
        return sesion.createQuery(
            "SELECT pp.personal FROM PersonalPrivilegio pp " +
            "WHERE pp.privilegio = :privilegio AND pp.activo = true", 
            Personal.class
        )
        .setParameter("privilegio", privilegio)
        .list();
    }

    public boolean tienePrivilegio(Personal personal, String nombrePrivilegio) {
        Long count = sesion.createQuery(
            "SELECT COUNT(pp) FROM PersonalPrivilegio pp " +
            "WHERE pp.personal = :personal AND pp.privilegio.nombre = :nombre AND pp.activo = true",
            Long.class
        )
        .setParameter("personal", personal)
        .setParameter("nombre", nombrePrivilegio)
        .uniqueResult();
        
        return count != null && count > 0;
    }

    // Método para cerrar la sesión
    public void cerrarSesion() {
        if (sesion != null && sesion.isOpen()) {
            sesion.close();
        }
    }
}
