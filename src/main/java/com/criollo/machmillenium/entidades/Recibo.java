package com.criollo.machmillenium.entidades;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recibos")
public class Recibo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    @Column(name = "ubicacion_recibo")
    private String ubicacionRecibo;

    @ManyToOne
    @JoinColumn(name = "pago_id", nullable = false)
    private Pago pago;
}
