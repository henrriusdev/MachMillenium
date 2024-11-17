package com.criollo.machmillenium.modelos;

import com.criollo.machmillenium.entidades.Cuota;
import com.criollo.machmillenium.entidades.Pago;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ModeloRecibo {
    private String nombreCliente;
    private String correoCliente;
    private String telefonoCliente;
    private String cedulaCliente;
    private String tipoObra;
    private String nombreObra;
    private String descripcionObra;
    private String estadoObra;
    private String costoObra;
    private String tiempoEstimadoObra;
    private String direccionObra;
    private String metodoPago;
    private String montoPago;
    private String porCuotas;
    private String numeroCuota;
    private String cuotasPorPagar;
    private String montoPorPagar;
    private String fecha;

    public ModeloRecibo() {
    }

    public ModeloRecibo(String nombreCliente, String correoCliente, String telefonoCliente, String cedulaCliente, String tipoObra, String nombreObra, String descripcionObra, String estadoObra, Double costoObra, Duration tiempoEstimadoObra, String direccionObra, String metodoPago, Double montoPago, boolean porCuotas, String numeroCuota, String cuotasPorPagar, Double montoPorPagar, LocalDateTime fecha) {
        this.nombreCliente = nombreCliente;
        this.correoCliente = correoCliente;
        this.telefonoCliente = telefonoCliente;
        this.cedulaCliente = cedulaCliente;
        this.tipoObra = tipoObra;
        this.nombreObra = nombreObra;
        this.descripcionObra = descripcionObra;
        this.estadoObra = estadoObra;
        this.costoObra = String.format("Bs. %.2f", costoObra);
        this.tiempoEstimadoObra = String.format("%d horas %d minutos", tiempoEstimadoObra.toHours(), tiempoEstimadoObra.toMinutesPart());
        this.direccionObra = direccionObra;
        this.metodoPago = metodoPago;
        this.montoPago = String.format("Bs. %.2f", montoPago);
        this.porCuotas = porCuotas ? "Si" : "No";
        this.numeroCuota = porCuotas ? numeroCuota : "N/A";
        this.cuotasPorPagar = porCuotas ? cuotasPorPagar : "N/A";
        this.montoPorPagar = porCuotas ? String.format("Bs. %.2f", montoPorPagar) : "N/A";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.fecha = fecha.format(formatter);
    }

    public ModeloRecibo(Pago pago) {
        this.nombreCliente = pago.getObra().getPresupuesto().getCliente().getNombre();
        this.correoCliente = pago.getObra().getPresupuesto().getCliente().getCorreo();
        this.telefonoCliente = pago.getObra().getPresupuesto().getCliente().getTelefono();
        this.cedulaCliente = pago.getObra().getPresupuesto().getCliente().getCedula();
        this.tipoObra = pago.getObra().getTipoObra().getNombre();
        this.nombreObra = pago.getObra().getNombre();
        this.descripcionObra = pago.getObra().getDescripcion();
        this.estadoObra = pago.getObra().getEstado();
        this.costoObra = String.format("Bs. %.2f", pago.getObra().getPresupuesto().getCosto());
        this.tiempoEstimadoObra = String.format("%d horas %d minutos", pago.getObra().getPresupuesto().getTiempoEstimado().toHours(), pago.getObra().getPresupuesto().getTiempoEstimado().toMinutesPart());
        this.direccionObra = pago.getObra().getPresupuesto().getDireccion();
        this.metodoPago = pago.getMetodoPago();
        this.montoPago = String.format("Bs. %.2f", pago.getMonto());
        this.porCuotas = pago.isCuotas() ? "Si" : "No";
        this.numeroCuota = pago.isCuotas() ? "5" : "N/A";
        this.cuotasPorPagar = pago.isCuotas() ? String.valueOf(pago.getCantidadCuotas() - 1) : "N/A";
        this.montoPorPagar = pago.isCuotas() ? String.format("Bs. %.2f", pago.getMonto() - pago.getObra().getPresupuesto().getCosto()) : "N/A";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.fecha = pago.getCreado().format(formatter);
    }

    public ModeloRecibo(Pago pago, List<Cuota> cuotas) {
        this.nombreCliente = pago.getObra().getPresupuesto().getCliente().getNombre();
        this.correoCliente = pago.getObra().getPresupuesto().getCliente().getCorreo();
        this.telefonoCliente = pago.getObra().getPresupuesto().getCliente().getTelefono();
        this.cedulaCliente = pago.getObra().getPresupuesto().getCliente().getCedula();
        this.tipoObra = pago.getObra().getTipoObra().getNombre();
        this.nombreObra = pago.getObra().getNombre();
        this.descripcionObra = pago.getObra().getDescripcion();
        this.estadoObra = pago.getObra().getEstado();
        this.costoObra = String.format("Bs. %.2f", pago.getObra().getPresupuesto().getCosto());
        this.tiempoEstimadoObra = String.format("%d horas %d minutos", pago.getObra().getPresupuesto().getTiempoEstimado().toHours(), pago.getObra().getPresupuesto().getTiempoEstimado().toMinutesPart());
        this.direccionObra = pago.getObra().getPresupuesto().getDireccion();
        this.metodoPago = pago.getMetodoPago();
        this.montoPago = String.format("Bs. %.2f", pago.getMonto());
        this.porCuotas = "Si";
        this.numeroCuota = String.valueOf(pago.getCantidadCuotas() - cuotas.size());
        this.cuotasPorPagar = String.valueOf(pago.getCantidadCuotas() - cuotas.size());
        this.montoPorPagar = String.format("Bs. %.2f", pago.getObra().getPresupuesto().getCosto() - cuotas.stream().mapToDouble(Cuota::getMonto).sum());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.fecha = cuotas.get(cuotas.size() - 1).getFecha().format(formatter);
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public String getTipoObra() {
        return tipoObra;
    }

    public void setTipoObra(String tipoObra) {
        this.tipoObra = tipoObra;
    }

    public String getNombreObra() {
        return nombreObra;
    }

    public void setNombreObra(String nombreObra) {
        this.nombreObra = nombreObra;
    }

    public String getDescripcionObra() {
        return descripcionObra;
    }

    public void setDescripcionObra(String descripcionObra) {
        this.descripcionObra = descripcionObra;
    }

    public String getEstadoObra() {
        return estadoObra;
    }

    public void setEstadoObra(String estadoObra) {
        this.estadoObra = estadoObra;
    }

    public String getCostoObra() {
        return costoObra;
    }

    public void setCostoObra(String costoObra) {
        this.costoObra = costoObra;
    }

    public String getTiempoEstimadoObra() {
        return tiempoEstimadoObra;
    }

    public void setTiempoEstimadoObra(String tiempoEstimadoObra) {
        this.tiempoEstimadoObra = tiempoEstimadoObra;
    }

    public String getDireccionObra() {
        return direccionObra;
    }

    public void setDireccionObra(String direccionObra) {
        this.direccionObra = direccionObra;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getMontoPago() {
        return montoPago;
    }

    public void setMontoPago(String montoPago) {
        this.montoPago = montoPago;
    }

    public String getPorCuotas() {
        return porCuotas;
    }

    public void setPorCuotas(String porCuotas) {
        this.porCuotas = porCuotas;
    }

    public String getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(String numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public String getCuotasPorPagar() {
        return cuotasPorPagar;
    }

    public void setCuotasPorPagar(String cuotasPorPagar) {
        this.cuotasPorPagar = cuotasPorPagar;
    }

    public String getMontoPorPagar() {
        return montoPorPagar;
    }

    public void setMontoPorPagar(String montoPorPagar) {
        this.montoPorPagar = montoPorPagar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
