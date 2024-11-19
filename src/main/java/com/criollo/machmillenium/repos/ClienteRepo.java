package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Cliente;
import com.criollo.machmillenium.modelos.ModeloCliente;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;

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
        Cliente clienteActual = obtenerPorId(cliente.getId());
        cliente.setCreado(clienteActual.getCreado());
        sesion.merge(cliente);
        sesion.getTransaction().commit();
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

    public List<ModeloCliente> obtenerClientesFiltrados(String nombre, String cedula, String correo, String sexo) {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Cliente> root = criteriaQuery.from(Cliente.class);

        List<Predicate> predicates = new ArrayList<>();

        if (nombre != null && !nombre.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("nombre"), "%" + nombre + "%"));
        }
        if (cedula != null && !cedula.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("cedula"), "%" + cedula + "%"));
        }
        if (correo != null && !correo.isEmpty()) {
            predicates.add(criteriaBuilder.like(root.get("correo"), "%" + correo + "%"));
        }
        if (sexo != null && !sexo.isEmpty() && !sexo.equals("Todos")) {
            predicates.add(criteriaBuilder.equal(root.get("sexo"), sexo));
        }
        predicates.add(criteriaBuilder.isNull(root.get("eliminado")));

        criteriaQuery.where(predicates.toArray(new Predicate[0]));
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
