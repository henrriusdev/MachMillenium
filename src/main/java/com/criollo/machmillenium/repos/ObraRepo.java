package com.criollo.machmillenium.repos;

import com.criollo.machmillenium.HibernateUtil;
import com.criollo.machmillenium.entidades.*;
import org.hibernate.Session;

import java.time.LocalDateTime;
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

    public void actualizarObra(Obra obra) {
        sesion.beginTransaction();
        sesion.merge(obra);
        sesion.getTransaction().commit();
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

    public void eliminarMaterial(Long id) {
        sesion.beginTransaction();
        ObraMaterial obraMaterial = sesion.get(ObraMaterial.class, id);
        sesion.remove(obraMaterial);
        sesion.getTransaction().commit();
    }

    public void insertarObraMaterial(List<ObraMaterial> obraMateriales) {
        sesion.beginTransaction();
        for (ObraMaterial obraMaterial : obraMateriales) {
            sesion.persist(obraMaterial);
        }
        sesion.getTransaction().commit();
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

    public void eliminarPersonal(Long id) {
        sesion.beginTransaction();
        ObraPersonal obraPersonal = sesion.get(ObraPersonal.class, id);
        sesion.remove(obraPersonal);
        sesion.getTransaction().commit();
    }

    public void insertarObraPersonal(List<ObraPersonal> obraPersonales) {
        sesion.beginTransaction();
        for (ObraPersonal obraPersonal : obraPersonales) {
            sesion.persist(obraPersonal);
        }
        sesion.getTransaction().commit();
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

    public void eliminarMaquinaria(Long id) {
        sesion.beginTransaction();
        ObraMaquinaria obraMaquinaria = sesion.get(ObraMaquinaria.class, id);
        sesion.remove(obraMaquinaria);
        sesion.getTransaction().commit();
    }

    public void insertarObraMaquinaria(List<ObraMaquinaria> obraMaquinarias) {
        sesion.beginTransaction();
        for (ObraMaquinaria obraMaquinaria : obraMaquinarias) {
            sesion.persist(obraMaquinaria);
        }
        sesion.getTransaction().commit();
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
}
