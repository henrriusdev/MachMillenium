package com.criollo.machmillenium.modelos;

public class ModeloInasistencia {
    private Long id;
    private String fecha;
    private String motivo;
    private String personal;

    public ModeloInasistencia() {
    }

    public ModeloInasistencia(Long id, String fecha, String motivo, String personal) {
        this.id = id;
        this.fecha = fecha;
        this.motivo = motivo;
        this.personal = personal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }
}
