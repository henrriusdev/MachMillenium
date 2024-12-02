package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.*;

import jakarta.persistence.criteria.*;

import org.hibernate.Session;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ObraRepo {
    private final Session sesion;

    public ObraRepo() {
        this.sesion = HibernateUtil.getSession();
    }

    public Obra insertarObra(Obra obra) {
        sesion.beginTransaction();
        sesion.persist(obra);
        sesion.getTransaction().commit();
        sesion.refresh(obra);
        return obra;
    }

    public List<Obra> obtenerObras() {
        sesion.beginTransaction();
        List<Obra> obras = sesion.createQuery("from Obra", Obra.class).list();
        sesion.getTransaction().commit();
        return obras;
    }

    public Obra obtenerObraPorId(Long id) {
        sesion.beginTransaction();
        Obra obra = sesion.get(Obra.class, id);
        sesion.getTransaction().commit();
        return obra;
    }

    public Obra obtenerPorNombre(String nombre) {
        sesion.beginTransaction();
        Obra obra = sesion.createQuery("from Obra where nombre = :nombre", Obra.class)
                .setParameter("nombre", nombre)
                .getSingleResult();
        sesion.getTransaction().commit();
        return obra;
    }

    public Obra actualizarObra(Obra obra) {
        sesion.beginTransaction();
        Obra obraActual = sesion.get(Obra.class, obra.getId());
        obra.setCreado(obraActual.getCreado());
        sesion.merge(obra);
        sesion.getTransaction().commit();
        sesion.refresh(obra);
        return obra;
    }

    public void eliminarObra(Long id) {
        sesion.beginTransaction();
        Obra obra = sesion.get(Obra.class, id);
        obra.setEliminado(LocalDateTime.now());
        sesion.merge(obra);
        sesion.getTransaction().commit();
    }

    public void insertarObraMaterial(ObraMaterial obraMaterial) {
        sesion.beginTransaction();
        sesion.persist(obraMaterial);
        sesion.getTransaction().commit();
    }

    public List<ObraMaterial> obtenerMaterialesPorObra(Long idObra) {
        sesion.beginTransaction();
        List<ObraMaterial> materiales = sesion.createQuery("from ObraMaterial where obra.id = :idObra", ObraMaterial.class)
                .setParameter("idObra", idObra)
                .list();
        sesion.getTransaction().commit();
        return materiales;
    }

    public void insertarObraPersonal(ObraPersonal obraPersonal) {
        sesion.beginTransaction();
        sesion.persist(obraPersonal);
        sesion.getTransaction().commit();
    }

    public List<ObraPersonal> obtenerPersonalPorObra(Long idObra) {
        sesion.beginTransaction();
        List<ObraPersonal> personal = sesion.createQuery("from ObraPersonal where obra.id = :idObra", ObraPersonal.class)
                .setParameter("idObra", idObra)
                .list();
        sesion.getTransaction().commit();
        return personal;
    }

    public void insertarObraMaquinaria(ObraMaquinaria obraMaquinaria) {
        sesion.beginTransaction();
        sesion.persist(obraMaquinaria);
        sesion.getTransaction().commit();
    }

    public List<ObraMaquinaria> obtenerMaquinariaPorObra(Long idObra) {
        sesion.beginTransaction();
        List<ObraMaquinaria> maquinaria = sesion.createQuery("from ObraMaquinaria where obra.id = :idObra", ObraMaquinaria.class)
                .setParameter("idObra", idObra)
                .list();
        sesion.getTransaction().commit();
        return maquinaria;
    }

    public List<TipoObra> obtenerTiposObra() {
        sesion.beginTransaction();
        List<TipoObra> tiposObra = sesion.createQuery("from TipoObra", TipoObra.class).list();
        sesion.getTransaction().commit();
        return tiposObra;
    }

    public List<Presupuesto> obtenerPresupuestos() {
        sesion.beginTransaction();
        List<Presupuesto> presupuestos = sesion.createQuery("from Presupuesto", Presupuesto.class).list();
        sesion.getTransaction().commit();
        return presupuestos;
    }

    public List<Personal> obtenerPersonal() {
        sesion.beginTransaction();
        List<Personal> personal = sesion.createQuery("from Personal", Personal.class).list();
        sesion.getTransaction().commit();
        return personal;
    }

    public List<Maquinaria> obtenerMaquinaria() {
        sesion.beginTransaction();
        List<Maquinaria> maquinaria = sesion.createQuery("from Maquinaria", Maquinaria.class).list();
        sesion.getTransaction().commit();
        return maquinaria;
    }

    public List<Material> obtenerMateriales() {
        sesion.beginTransaction();
        List<Material> materiales = sesion.createQuery("from Material", Material.class).list();
        sesion.getTransaction().commit();
        return materiales;
    }

    public Presupuesto obtenerPresupuestoPorDescripcion(String selectedItem) {
        sesion.beginTransaction();
        Presupuesto presupuesto = sesion.createQuery("from Presupuesto where descripcion = :descripcion", Presupuesto.class)
                .setParameter("descripcion", selectedItem)
                .getSingleResult();
        sesion.getTransaction().commit();
        return presupuesto;
    }

    public TipoObra obtenerTipoObraPorNombre(String selectedItem) {
        sesion.beginTransaction();
        TipoObra tipoObra = sesion.createQuery("from TipoObra where nombre = :nombre", TipoObra.class)
                .setParameter("nombre", selectedItem)
                .getSingleResult();
        sesion.getTransaction().commit();
        return tipoObra;
    }

    public Material obtenerMaterialPorNombre(String materialPart) {
        sesion.beginTransaction();
        Material material = sesion.createQuery("from Material where nombre = :nombre", Material.class)
                .setParameter("nombre", materialPart)
                .getSingleResult();
        sesion.getTransaction().commit();
        return material;
    }

    public Personal obtenerPersonalPorNombre(String personalPart) {
        sesion.beginTransaction();
        Personal personal = sesion.createQuery("from Personal where nombre = :nombre", Personal.class)
                .setParameter("nombre", personalPart)
                .getSingleResult();
        sesion.getTransaction().commit();
        return personal;
    }

    public Maquinaria obtenerMaquinariaPorNombre(String maquinariaPart) {
        sesion.beginTransaction();
        Maquinaria maquinaria = sesion.createQuery("from Maquinaria where nombre = :nombre", Maquinaria.class)
                .setParameter("nombre", maquinariaPart)
                .getSingleResult();
        sesion.getTransaction().commit();
        return maquinaria;
    }

    public void insertarTipoObra(TipoObra tipoObra) {
        sesion.beginTransaction();
        sesion.persist(tipoObra);
        sesion.getTransaction().commit();
    }

    public Long contarTipoObra() {
        sesion.beginTransaction();
        Long count = sesion.createQuery("select count(*) from TipoObra", Long.class).uniqueResult();
        sesion.getTransaction().commit();
        return count;
    }

    public void eliminarObraMaterial(ObraMaterial obraMaterial) {
        sesion.beginTransaction();
        sesion.remove(obraMaterial);
        sesion.getTransaction().commit();
    }

    public void eliminarObraPersonal(ObraPersonal obraPersonal) {
        sesion.beginTransaction();
        sesion.remove(obraPersonal);
        sesion.getTransaction().commit();
    }

    public void eliminarObraMaquinaria(ObraMaquinaria obraMaquinaria) {
        sesion.beginTransaction();
        sesion.remove(obraMaquinaria);
        sesion.getTransaction().commit();
    }

    public List<Obra> obtenerObrasFiltradas(String nombre, String estado, String clienteNombre, 
                                          String tipoObra, Double presupuestoMin, Double presupuestoMax) {
        CriteriaBuilder cb = sesion.getCriteriaBuilder();
        CriteriaQuery<Obra> query = cb.createQuery(Obra.class);
        Root<Obra> obra = query.from(Obra.class);
        Join<Obra, Presupuesto> presupuesto = obra.join("presupuesto", JoinType.LEFT);
        Join<Presupuesto, Cliente> cliente = presupuesto.join("cliente", JoinType.LEFT);
        Join<Obra, TipoObra> tipoObraJoin = obra.join("tipoObra", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        // Filtro por nombre
        if (nombre != null && !nombre.isEmpty()) {
            predicates.add(cb.like(cb.lower(obra.get("nombre")), "%" + nombre.toLowerCase() + "%"));
        }

        // Filtro por estado
        if (estado != null && !estado.isEmpty() && !estado.equals("Todos")) {
            predicates.add(cb.equal(obra.get("estado"), estado));
        }

        // Filtro por nombre del cliente
        if (clienteNombre != null && !clienteNombre.isEmpty()) {
            predicates.add(cb.like(cb.lower(cliente.get("nombre")), "%" + clienteNombre.toLowerCase() + "%"));
        }

        // Filtro por tipo de obra
        if (tipoObra != null && !tipoObra.isEmpty() && !tipoObra.equals("Todos")) {
            predicates.add(cb.equal(tipoObraJoin.get("nombre"), tipoObra));
        }

        // Filtro por rango de presupuesto
        if (presupuestoMin != null) {
            predicates.add(cb.greaterThanOrEqualTo(presupuesto.get("costo"), presupuestoMin));
        }
        if (presupuestoMax != null) {
            predicates.add(cb.lessThanOrEqualTo(presupuesto.get("costo"), presupuestoMax));
        }

        // Excluir registros eliminados lógicamente
        predicates.add(cb.isNull(obra.get("eliminado")));

        query.where(predicates.toArray(new Predicate[0]));
        query.orderBy(cb.asc(obra.get("nombre")));

        return sesion.createQuery(query).getResultList();
    }

    public List<Objetivo> obtenerObjetivosPorObra(Long idObra) {
        sesion.beginTransaction();
        List<Objetivo> objetivos = sesion.createQuery("from Objetivo where obra.id = :idObra", Objetivo.class)
                .setParameter("idObra", idObra)
                .list();
        sesion.getTransaction().commit();
        return objetivos;
    }

    public Objetivo insertarObjetivo(Objetivo objetivo) {
        sesion.beginTransaction();
        sesion.persist(objetivo);
        sesion.getTransaction().commit();
        sesion.refresh(objetivo);
        return objetivo;
    }

    public Objetivo actualizarObjetivo(Objetivo objetivo) {
        sesion.beginTransaction();
        Objetivo objetivoActual = sesion.get(Objetivo.class, objetivo.getId());
        objetivo.setCreado(objetivoActual.getCreado());
        if (objetivo.isCompletado() && objetivoActual.getCompletadoEl() == null) {
            objetivo.setCompletadoEl(LocalDateTime.now());
        }
        objetivo.setModificado(LocalDateTime.now());
        sesion.merge(objetivo);
        sesion.getTransaction().commit();
        sesion.refresh(objetivo);
        return objetivo;
    }

    public void eliminarObjetivo(Long id) {
        sesion.beginTransaction();
        Objetivo objetivo = sesion.get(Objetivo.class, id);
        sesion.remove(objetivo);
        sesion.getTransaction().commit();
    }
}
