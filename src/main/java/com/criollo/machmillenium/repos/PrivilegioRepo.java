package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.entidades.Privilegio;
import com.criollo.machmillenium.entidades.RolPrivilegio;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class PrivilegioRepo {
    
    public List<Privilegio> obtenerPrivilegiosPorRol(String rol) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "SELECT rp.privilegio FROM RolPrivilegio rp WHERE rp.rol = :rol", 
                Privilegio.class
            )
            .setParameter("rol", rol)
            .list();
        }
    }
    
    public void guardarPrivilegio(Privilegio privilegio) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(privilegio);
            tx.commit();
        }
    }
    
    public void asignarPrivilegioARol(String rol, Privilegio privilegio) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            RolPrivilegio rolPrivilegio = new RolPrivilegio();
            rolPrivilegio.setRol(rol);
            rolPrivilegio.setPrivilegio(privilegio);
            session.save(rolPrivilegio);
            tx.commit();
        }
    }
}
