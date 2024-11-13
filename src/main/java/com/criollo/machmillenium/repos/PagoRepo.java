package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Pago;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.List;

public class PagoRepo {
    private final Session sesion;

    public PagoRepo() {
        this.sesion = HibernateUtil.getSession();
    }

    public void registrarPago(Pago pago) {
        sesion.beginTransaction();
        sesion.persist(pago);
        sesion.getTransaction().commit();
    }

    public Pago obtenerPagoPorId(Long id) {
        sesion.beginTransaction();
        Pago pago = sesion.get(Pago.class, id);
        sesion.getTransaction().commit();
        return pago;
    }
}
