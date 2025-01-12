package com.criollo.machmillenium.enums;

public enum Privilegios {
    // Privilegios para Clientes
    VER_CLIENTES("Permite ver la lista de clientes"),
    CREAR_CLIENTES("Permite crear nuevos clientes"),
    MODIFICAR_CLIENTES("Permite modificar clientes existentes"),
    
    // Privilegios para Personal
    VER_PERSONAL("Permite ver la lista de personal"),
    CREAR_PERSONAL("Permite crear nuevo personal"),
    MODIFICAR_PERSONAL("Permite modificar personal existente"),
    
    // Privilegios para Tipo Maquinaria
    VER_TIPO_MAQUINARIA("Permite ver tipos de maquinaria"),
    CREAR_TIPO_MAQUINARIA("Permite crear tipos de maquinaria"),
    MODIFICAR_TIPO_MAQUINARIA("Permite modificar tipos de maquinaria"),
    
    // Privilegios para Maquinaria
    VER_MAQUINARIA("Permite ver maquinarias"),
    CREAR_MAQUINARIA("Permite crear maquinarias"),
    MODIFICAR_MAQUINARIA("Permite modificar maquinarias"),
    
    // Privilegios para Tipo Insumo
    VER_TIPO_INSUMO("Permite ver tipos de insumo"),
    CREAR_TIPO_INSUMO("Permite crear tipos de insumo"),
    MODIFICAR_TIPO_INSUMO("Permite modificar tipos de insumo"),
    
    // Privilegios para Materiales
    VER_MATERIALES("Permite ver materiales"),
    CREAR_MATERIALES("Permite crear materiales"),
    MODIFICAR_MATERIALES("Permite modificar materiales"),
    
    // Privilegios para Presupuesto
    VER_PRESUPUESTO("Permite ver presupuestos"),
    CREAR_PRESUPUESTO("Permite crear presupuestos"),
    MODIFICAR_PRESUPUESTO("Permite modificar presupuestos"),
    
    // Privilegios para Obras
    VER_OBRAS("Permite ver obras"),
    CREAR_OBRAS("Permite crear obras"),
    MODIFICAR_OBRAS("Permite modificar obras"),
    
    // Privilegios para Auditoria
    VER_AUDITORIA("Permite ver auditoría"),
    CREAR_AUDITORIA("Permite crear registros de auditoría"),
    MODIFICAR_AUDITORIA("Permite modificar registros de auditoría"),
    
    // Privilegios para Inasistencias
    VER_INASISTENCIAS("Permite ver inasistencias"),
    CREAR_INASISTENCIAS("Permite registrar inasistencias"),
    MODIFICAR_INASISTENCIAS("Permite modificar inasistencias"),
    
    // Privilegios para Recibos
    VER_RECIBOS("Permite ver recibos"),
    CREAR_RECIBOS("Permite crear recibos"),
    MODIFICAR_RECIBOS("Permite modificar recibos");

    private final String descripcion;

    Privilegios(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
