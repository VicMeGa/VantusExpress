package com.vantus.vantusexpress.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "folio", unique = true, length = 20)
    private String folio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinatario_id", nullable = false)
    private Destinatario destinatario;

    @Column(name = "contenido", columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "valor_estimado", precision = 10, scale = 2)
    private BigDecimal valorEstimado;

    @Column(name = "estado", length = 50)
    private String estado;

    @Column(name = "fecha", updatable = false)
    private LocalDateTime fecha;

    @PrePersist
    public void asignarDefaults() {
        this.fecha = LocalDateTime.now();
        if (this.estado == null) this.estado = "registrado";
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getFolio() { return folio; }
    public void setFolio(String folio) { this.folio = folio; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Destinatario getDestinatario() { return destinatario; }
    public void setDestinatario(Destinatario destinatario) { this.destinatario = destinatario; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
    public BigDecimal getValorEstimado() { return valorEstimado; }
    public void setValorEstimado(BigDecimal valorEstimado) { this.valorEstimado = valorEstimado; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}