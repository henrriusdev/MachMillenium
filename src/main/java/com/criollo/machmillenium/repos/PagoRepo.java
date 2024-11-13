package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Pago;
import com.criollo.machmillenium.entidades.Recibo;
import com.criollo.machmillenium.vistas.emergentes.clientes.RegistrarPago;
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

    public void registrarRecibo(Recibo recibo) {
        sesion.beginTransaction();
        sesion.persist(recibo);
        sesion.getTransaction().commit();
    }

    public Recibo obtenerReciboPorId(Long id) {
        sesion.beginTransaction();
        Recibo recibo = sesion.get(Recibo.class, id);
        sesion.getTransaction().commit();
        return recibo;
    }

    public Recibo actualizarRecibo(Recibo recibo) {
        sesion.beginTransaction();
        Recibo reciboActual = obtenerReciboPorId(recibo.getId());
        recibo.setModificado(LocalDateTime.now());
        sesion.merge(recibo);
        sesion.getTransaction().commit();
        sesion.refresh(recibo);
        return recibo;
    }

    public List<Recibo> obtenerRecibosPorPago(Long idPago) {
        sesion.beginTransaction();
        // get recibos by idPago
        List<Recibo> recibos = sesion.createQuery("from Recibo where pago.id = :idPago", Recibo.class)
                .setParameter("idPago", idPago)
                .list();
        sesion.getTransaction().commit();
        return recibos;
    }
}
