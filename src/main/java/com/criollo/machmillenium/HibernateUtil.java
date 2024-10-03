package com.criollo.machmillenium;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    // Instancia única de SessionFactory
    private static SessionFactory sessionFactory;

    // Constructor privado para evitar la creación de instancias
    private HibernateUtil() {}

    // Method para obtener la instancia de SessionFactory
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Configurar Hibernate y construir la SessionFactory
                sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
            } catch (Throwable ex) {
                System.err.println("Initial SessionFactory creation failed." + ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
        return sessionFactory;
    }

    // Method para obtener una sesión de Hibernate
    public static Session getSession() {
        return getSessionFactory().openSession();
    }

    // Method para cerrar la SessionFactory (opcional para liberar recursos)
    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}