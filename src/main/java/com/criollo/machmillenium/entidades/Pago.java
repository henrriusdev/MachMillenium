package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "pagos")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "metodo_pago", nullable = false)
    private String metodoPago;

    private boolean cuotas;
    private int cantidadCuotas;

    private Double monto;

    @ManyToOne
    @JoinColumn(name = "obra_id", nullable = false)
    private Obra obra;

}
