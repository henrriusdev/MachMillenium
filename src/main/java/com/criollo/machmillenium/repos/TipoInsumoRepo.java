package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Material;
import com.criollo.machmillenium.entidades.TipoInsumo;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TipoInsumoRepo {
    private final Session sesion;

    public TipoInsumoRepo() {
        this.sesion = HibernateUtil.getSession();
    }

    public void insertar(TipoInsumo tipoInsumo) {
        sesion.beginTransaction();
        sesion.persist(tipoInsumo);
        sesion.getTransaction().commit();
    }

    public void actualizar(TipoInsumo tipoInsumo) {
        sesion.beginTransaction();
        TipoInsumo tipoInsumoActual = sesion.get(TipoInsumo.class, tipoInsumo.getId());
        tipoInsumo.setCreado(tipoInsumoActual.getCreado());
        sesion.merge(tipoInsumo);
        sesion.getTransaction().commit();
    }

    public TipoInsumo obtenerPorNombre(String nombre) {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<TipoInsumo> criteriaQuery = criteriaBuilder.createQuery(TipoInsumo.class);
        Root<TipoInsumo> root = criteriaQuery.from(TipoInsumo.class);
        criteriaQuery.select(root).where(criteriaBuilder.and(criteriaBuilder.isNull(root.get("eliminado")), criteriaBuilder.equal(root.get("nombre"), nombre)));
        TipoInsumo tipoInsumo = sesion.createQuery(criteriaQuery).getSingleResult();
        sesion.getTransaction().commit();
        return tipoInsumo;
    }

    public void eliminar(Long id) {
        sesion.beginTransaction();
        TipoInsumo tipoInsumo = sesion.get(TipoInsumo.class, id);
        tipoInsumo.setEliminado(LocalDateTime.now());
        sesion.merge(tipoInsumo);
        sesion.getTransaction().commit();
    }

    public TipoInsumo obtenerPorId(Long id) {
        sesion.beginTransaction();
        TipoInsumo tipoInsumo = sesion.get(TipoInsumo.class, id);
        sesion.getTransaction().commit();
        return tipoInsumo;
    }

    // Obtener todos los registros de TipoInsumo que no están eliminados (eliminado es null)
    public List<TipoInsumo> obtenerTodos() {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<TipoInsumo> criteriaQuery = criteriaBuilder.createQuery(TipoInsumo.class);

        // Usar un solo root para la entidad
        Root<TipoInsumo> root = criteriaQuery.from(TipoInsumo.class);

        // Construir la consulta con el criterio de "eliminado" nulo
        criteriaQuery.select(root)
                .where(criteriaBuilder.isNull(root.get("eliminado")));

        // Ejecutar la consulta
        List<TipoInsumo> tipoInsumoList = sesion.createQuery(criteriaQuery).getResultList();
        sesion.getTransaction().commit();
        return tipoInsumoList;
    }

    // Insertar un nuevo registro de Material
    public void insertarMaterial(Material material) {
        sesion.beginTransaction();
        sesion.persist(material);
        sesion.getTransaction().commit();
    }

    // Actualizar un registro existente de Material
    public void actualizarMaterial(Material material) {
        sesion.beginTransaction();
        sesion.merge(material);
        sesion.getTransaction().commit();
    }

    // Eliminar lógicamente un registro de Material, marcando su campo "eliminado"
    public void eliminarMaterial(Long id) {
        sesion.beginTransaction();
        Material material = sesion.get(Material.class, id);

        if (material != null) {
            material.setEliminado(LocalDateTime.now());
            sesion.merge(material);  // Actualizar el estado del objeto en la base de datos
        }

        sesion.getTransaction().commit();
    }

    // Obtener todos los registros de Material que no están eliminados (eliminado es null)
    public List<Material> obtenerMateriales() {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Material> criteriaQuery = criteriaBuilder.createQuery(Material.class);

        // Usar un solo root para la entidad
        Root<Material> root = criteriaQuery.from(Material.class);

        // Construir la consulta con el criterio de "eliminado" nulo
        criteriaQuery.select(root)
                .where(criteriaBuilder.isNull(root.get("eliminado")));

        // Ejecutar la consulta
        List<Material> materialList = sesion.createQuery(criteriaQuery).getResultList();
        sesion.getTransaction().commit();
        return materialList;
    }

    // Obtener materiales filtrados por nombre, costo y tipo de insumo
    public List<Material> obtenerMaterialesFiltrados(String nombre, Double costoMin, Double costoMax, String tipoInsumo) {
        sesion.beginTransaction();
        CriteriaBuilder builder = sesion.getCriteriaBuilder();
        CriteriaQuery<Material> query = builder.createQuery(Material.class);
        Root<Material> root = query.from(Material.class);
        Join<Material, TipoInsumo> tipoJoin = root.join("tipoInsumo", JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();

        // Filtrar por nombre si se proporciona
        if (nombre != null && !nombre.isEmpty()) {
            predicates.add(builder.like(builder.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
        }

        // Filtrar por costo mínimo si se proporciona
        if (costoMin != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("costo"), costoMin));
        }

        // Filtrar por costo máximo si se proporciona
        if (costoMax != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("costo"), costoMax));
        }

        // Filtrar por tipo de insumo si se proporciona y no es "Todos"
        if (tipoInsumo != null && !tipoInsumo.equals("Todos")) {
            predicates.add(builder.equal(tipoJoin.get("nombre"), tipoInsumo));
        }

        // Agregar predicado para excluir registros eliminados
        predicates.add(builder.isNull(root.get("eliminado")));

        // Aplicar todos los predicados a la consulta
        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0]));
        }

        // Ejecutar la consulta y devolver los resultados
        List<Material> materialList = sesion.createQuery(query).getResultList();
        sesion.getTransaction().commit();
        return materialList;
    }
}
