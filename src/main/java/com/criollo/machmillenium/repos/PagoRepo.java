package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Cuota;
import com.criollo.machmillenium.entidades.Obra;
import com.criollo.machmillenium.entidades.Pago;
import jakarta.persistence.Query;
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

    public List<Cuota> obtenerCuotasPorPagoId(Long id) {
        sesion.beginTransaction();
        List<Cuota> cuotas = sesion.createQuery("from Cuota where pago.id = :id", Cuota.class)
                .setParameter("id", id)
                .getResultList();
        sesion.getTransaction().commit();
        return cuotas;
    }

    public List<Pago> obtenerPagosPorCuotas(boolean cuotas) {
        sesion.beginTransaction();
        List<Pago> pagos = sesion.createQuery("from Pago where cuotas = :cuotas", Pago.class)
                .setParameter("cuotas", cuotas)
                .getResultList();
        sesion.getTransaction().commit();
        return pagos;
    }

    public Long obtenerCantidadCuotasPorPagoId(Long id) {
        sesion.beginTransaction();
        Long cantidad = (Long) sesion.createQuery("select count(*) from Cuota where pago.id = :id")
                .setParameter("id", id)
                .getSingleResult();
        sesion.getTransaction().commit();
        return cantidad;
    }

    public void registrarCuota(Cuota cuota) {
        sesion.beginTransaction();
        sesion.persist(cuota);
        sesion.getTransaction().commit();
    }
}
