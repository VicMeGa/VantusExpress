package com.vantus.vantusexpress.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.Map;

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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "datos", columnDefinition = "JSON")
    private Map<String, Object> datos;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void actualizarTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCallSid() { return callSid; }
    public void setCallSid(String callSid) { this.callSid = callSid; }
    public String getPasoActual() { return pasoActual; }
    public void setPasoActual(String pasoActual) { this.pasoActual = pasoActual; }
    public Map<String, Object> getDatos() { return datos; }
    public void setDatos(Map<String, Object> datos) { this.datos = datos; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}