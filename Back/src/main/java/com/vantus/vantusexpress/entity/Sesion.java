package com.vantus.vantusexpress.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sesiones")
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "call_sid", unique = true, nullable = false, length = 50)
    private String callSid;

    @Column(name = "paso_actual", length = 50)
    private String pasoActual;

    @Column(name = "datos", columnDefinition = "TEXT")
    private String datos;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void actualizarTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCallSid() { return callSid; }
    public void setCallSid(String callSid) { this.callSid = callSid; }
    public String getPasoActual() { return pasoActual; }
    public void setPasoActual(String pasoActual) { this.pasoActual = pasoActual; }
    public String getDatos() { return datos; }
    public void setDatos(String datos) { this.datos = datos; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
