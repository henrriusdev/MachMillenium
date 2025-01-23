package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.entidades.PreguntasSeguridad;
import com.criollo.machmillenium.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class PreguntasSeguridadRepo {
    
    public void insertar(PreguntasSeguridad preguntasSeguridad) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(preguntasSeguridad);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void actualizar(PreguntasSeguridad preguntasSeguridad) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(preguntasSeguridad);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public PreguntasSeguridad obtenerPorPersonal(Personal personal) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<PreguntasSeguridad> query = session.createQuery(
                "FROM PreguntasSeguridad p WHERE p.personal = :personal AND p.eliminado IS NULL", 
                PreguntasSeguridad.class);
            query.setParameter("personal", personal);
            return query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean verificarRespuestas(Personal personal, String respuesta1, String respuesta2, String respuesta3) {
        PreguntasSeguridad preguntas = obtenerPorPersonal(personal);
        if (preguntas == null) {
            return false;
        }
        
        return preguntas.getRespuesta1().equals(respuesta1) &&
               preguntas.getRespuesta2().equals(respuesta2) &&
               preguntas.getRespuesta3().equals(respuesta3);
    }
}
