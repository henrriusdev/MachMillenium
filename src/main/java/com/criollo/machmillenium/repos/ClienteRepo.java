package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Cliente;
import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.modelos.ModeloCliente;
import com.criollo.machmillenium.modelos.ModeloPersonal;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.lang.ref.Cleaner;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepo {
    private final Session sesion;

    public ClienteRepo() {
        this.sesion = HibernateUtil.getSession();
    }

    public void insertar(Cliente cliente) {
        sesion.beginTransaction();
        sesion.persist(cliente);
        sesion.getTransaction().commit();
    }

    public void actualizar(Cliente cliente) {
        sesion.beginTransaction();
        System.out.println(cliente);
        sesion.merge(cliente);
        sesion.getTransaction().commit();
    }

    public void eliminar(Cliente cliente) {
        sesion.beginTransaction();
        sesion.remove(cliente);
        sesion.getTransaction().commit();
    }

    public Long contar() {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Cliente.class)));
        Long cantidad = sesion.createQuery(criteriaQuery).getSingleResult();
        sesion.getTransaction().commit();

        return cantidad;
    }

    public Cliente obtenerPorId(Long id) {
        sesion.beginTransaction();
        Cliente cliente = sesion.get(Cliente.class, id);
        sesion.getTransaction().commit();

        return cliente;
    }

    public Cliente obtenerPorNombre(String nombre) {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Cliente> root = criteriaQuery.from(Cliente.class);
        criteriaQuery.select(root).where(criteriaBuilder.and(criteriaBuilder.isNull(root.get("eliminado")), criteriaBuilder.equal(root.get("nombre"), nombre)));
        Query<Cliente> query = sesion.createQuery(criteriaQuery);
        Cliente cliente = query.getSingleResult();
        sesion.getTransaction().commit();

        return cliente;
    }

    public List<ModeloCliente> obtenerTodos() {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        criteriaQuery.from(Cliente.class);
        Query<Cliente> query = sesion.createQuery(criteriaQuery);
        List<Cliente> clienteList = query.getResultList();
        sesion.getTransaction().commit();

        List<ModeloCliente> modeloClienteList = new ArrayList<>();
        for (Cliente cliente : clienteList) {
            modeloClienteList.add(new ModeloCliente(cliente));
        }

        return modeloClienteList;
    }
}
