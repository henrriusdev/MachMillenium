package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.Especialidad;
import com.criollo.machmillenium.entidades.Inasistencia;
import com.criollo.machmillenium.entidades.Personal;
import com.criollo.machmillenium.entidades.Rol;
import com.criollo.machmillenium.modelos.ModeloInasistencia;
import com.criollo.machmillenium.modelos.ModeloPersonal;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PersonalRepo {
    private final Session sesion;

    public PersonalRepo() {
        this.sesion = HibernateUtil.getSession();
    }

    public List<ModeloPersonal> obtenerTodos() {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Personal> criteriaQuery = criteriaBuilder.createQuery(Personal.class);
        criteriaQuery.from(Personal.class);
        Query<Personal> query = sesion.createQuery(criteriaQuery);
        List<Personal> personalList = query.getResultList();
        sesion.getTransaction().commit();

        List<ModeloPersonal> modeloPersonalList = new ArrayList<>();
        for (Personal personal : personalList) {
            modeloPersonalList.add(new ModeloPersonal(personal));
        }

        return modeloPersonalList;
    }

    public Personal insertar(Personal personal) {
        sesion.beginTransaction();
        sesion.persist(personal);
        sesion.refresh(personal);
        sesion.getTransaction().commit();

        return personal;
    }

    public void actualizar(Personal personal) {
        sesion.beginTransaction();
        sesion.merge(personal);
        sesion.getTransaction().commit();
    }

    public Long contar() {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Personal.class)));
        Query<Long> query = sesion.createQuery(criteriaQuery);
        Long count = query.getSingleResult();
        sesion.getTransaction().commit();
        return count;
    }

    public Personal obtenerPorCorreo(String correo) {
        try{
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Personal> criteriaQuery = criteriaBuilder.createQuery(Personal.class);
        criteriaQuery.where(criteriaBuilder.equal(criteriaQuery.from(Personal.class).get("correo"), correo));
        Query<Personal> query = sesion.createQuery(criteriaQuery);
        Personal personal = query.getSingleResult();
        sesion.getTransaction().commit();
        return personal;
        }catch (Exception e){
            return null;
        }
    }

    public Personal obtenerPorNombre(String nombre) {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Personal> criteriaQuery = criteriaBuilder.createQuery(Personal.class);
        criteriaQuery.where(criteriaBuilder.equal(criteriaQuery.from(Personal.class).get("nombre"), nombre));
        Query<Personal> query = sesion.createQuery(criteriaQuery);
        Personal personal = query.getSingleResult();
        sesion.getTransaction().commit();
        return personal;
    }

    public Especialidad obtenerEspecialidadPorNombre(String nombre) {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Especialidad> criteriaQuery = criteriaBuilder.createQuery(Especialidad.class);
        criteriaQuery.where(criteriaBuilder.equal(criteriaQuery.from(Especialidad.class).get("nombre"), nombre));
        Query<Especialidad> query = sesion.createQuery(criteriaQuery);
        Especialidad especialidad = query.getSingleResult();
        sesion.getTransaction().commit();
        return especialidad;
    }

    public Rol obtenerRolPorNombre(String nombre) {
        sesion.beginTransaction();
        Rol rol = sesion.bySimpleNaturalId(Rol.class).load(nombre);
        sesion.getTransaction().commit();
        return rol;
    }

    public Especialidad obtenerOCrearEspecialidad(String nombre) {
        Especialidad especialidad = obtenerEspecialidadPorNombre(nombre);
        if (especialidad == null) {
            especialidad = new Especialidad();
            especialidad.setNombre(nombre);
            sesion.beginTransaction();
            sesion.persist(especialidad);
            sesion.getTransaction().commit();
        }

        return especialidad;
    }

    public void cambiarClave(Long idPersonal, String hashpw) {
        sesion.beginTransaction();
        Personal personal = sesion.get(Personal.class, idPersonal);
        personal.setClave(hashpw);
        sesion.getTransaction().commit();
    }

    public List<Personal> obtenerPorRol(String rol) {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Personal> criteriaQuery = criteriaBuilder.createQuery(Personal.class);
        criteriaQuery.where(criteriaBuilder.equal(criteriaQuery.from(Personal.class).get("rol").get("nombre"), rol));
        Query<Personal> query = sesion.createQuery(criteriaQuery);
        List<Personal> personalList = query.getResultList();
        sesion.getTransaction().commit();
        return personalList;
    }

    public List<Inasistencia> obtenerInasistencias() {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Inasistencia> criteriaQuery = criteriaBuilder.createQuery(Inasistencia.class);
        criteriaQuery.from(Inasistencia.class);
        Query<Inasistencia> query = sesion.createQuery(criteriaQuery);
        List<Inasistencia> inasistenciaList = query.getResultList();
        sesion.getTransaction().commit();
        return inasistenciaList;
    }

    public Inasistencia obtenerInasistenciaPorId(Long idInasistencia) {
        sesion.beginTransaction();
        Inasistencia inasistencia = sesion.get(Inasistencia.class, idInasistencia);
        sesion.getTransaction().commit();
        return inasistencia;
    }

    public void actualizarMotivoInasistencia(Long idInasistencia, String motivo) {
        Inasistencia inasistencia = obtenerInasistenciaPorId(idInasistencia);
        inasistencia.setMotivo(motivo);
        inasistencia.setModificado(LocalDateTime.now());
        sesion.beginTransaction();
        sesion.merge(inasistencia);
        sesion.getTransaction().commit();
    }

    public  void registrarInasistencias(List<Inasistencia> inasistencias) {
        sesion.beginTransaction();
        for (Inasistencia inasistencia : inasistencias) {
            sesion.persist(inasistencia);
        }
        sesion.getTransaction().commit();
    }

    public List<ModeloInasistencia> obtenerInasistenciasModelo() {
        List<Inasistencia> inasistenciaList = obtenerInasistencias();
        List<ModeloInasistencia> modeloInasistenciaList = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Inasistencia inasistencia : inasistenciaList) {
            modeloInasistenciaList.add(new ModeloInasistencia(inasistencia.getId(), inasistencia.getFecha().format(dateTimeFormatter), inasistencia.getMotivo(), inasistencia.getPersonal().getNombre()));
        }
        return modeloInasistenciaList;
    }

    public List<ModeloPersonal> obtenerTodosPorFiltros(String nombre, String cedula, String correo, Boolean fijo, Boolean activo, String especialidad, String rol) {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Personal> criteriaQuery = criteriaBuilder.createQuery(Personal.class);
        var root = criteriaQuery.from(Personal.class);
        var predicates = new ArrayList<jakarta.persistence.criteria.Predicate>();

        // Only add filter if nombre is not null and not empty
        if (nombre != null && !nombre.trim().isEmpty()) {
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get("nombre")), 
                "%" + nombre.toLowerCase().trim() + "%"
            ));
        }

        // Only add filter if cedula is not null and not empty
        if (cedula != null && !cedula.trim().isEmpty()) {
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get("cedula")), 
                "%" + cedula.toLowerCase().trim() + "%"
            ));
        }

        // Only add filter if correo is not null and not empty
        if (correo != null && !correo.trim().isEmpty()) {
            predicates.add(criteriaBuilder.like(
                criteriaBuilder.lower(root.get("correo")), 
                "%" + correo.toLowerCase().trim() + "%"
            ));
        }

        // Only add filter if fijo is not null
        if (fijo != null) {
            predicates.add(criteriaBuilder.equal(root.get("fijo"), fijo));
        }

        // Only add filter if activo is not null
        if (activo != null) {
            predicates.add(criteriaBuilder.equal(root.get("activo"), activo));
        }

        // Only add filter if especialidad is not null, not empty, and not "Todos"
        if (especialidad != null && !especialidad.trim().isEmpty() && !"Todos".equals(especialidad)) {
            predicates.add(criteriaBuilder.equal(
                root.get("especialidad").get("nombre"), 
                especialidad.trim()
            ));
        }

        // Only add filter if rol is not null, not empty, and not "Todos"
        if (rol != null && !rol.trim().isEmpty() && !"Todos".equals(rol)) {
            predicates.add(criteriaBuilder.equal(
                root.get("rol").get("nombre"), 
                rol.trim()
            ));
        }

        // Only apply where clause if there are predicates
        if (!predicates.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0])));
        }

        Query<Personal> query = sesion.createQuery(criteriaQuery);
        List<Personal> personalList = query.getResultList();
        sesion.getTransaction().commit();

        return personalList.stream()
            .map(ModeloPersonal::new)
            .collect(java.util.stream.Collectors.toList());
    }

    public void restablecerClave(Long idPersonal, String nuevaClave) {
        sesion.beginTransaction();
        Personal personal = sesion.get(Personal.class, idPersonal);
        personal.setClave(nuevaClave);
        personal.setModificado(LocalDateTime.now());
        sesion.merge(personal);
        sesion.getTransaction().commit();
    }

    public Personal obtenerPorId(Long idPersonal) {
        sesion.beginTransaction();
        Personal personal = sesion.get(Personal.class, idPersonal);
        sesion.getTransaction().commit();
        return personal;
    }

    public boolean existePorCedula(String cedula) {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Personal.class)));
        criteriaQuery.where(criteriaBuilder.equal(criteriaQuery.from(Personal.class).get("cedula"), cedula));
        Query<Long> query = sesion.createQuery(criteriaQuery);
        Long count = query.getSingleResult();
        sesion.getTransaction().commit();
        return count > 0;
    }

    public boolean existePorCorreo(String correo) {
        sesion.beginTransaction();
        CriteriaBuilder criteriaBuilder = sesion.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(Personal.class)));
        criteriaQuery.where(criteriaBuilder.equal(criteriaQuery.from(Personal.class).get("correo"), correo));
        Query<Long> query = sesion.createQuery(criteriaQuery);
        Long count = query.getSingleResult();
        sesion.getTransaction().commit();
        return count > 0;
    }
}
