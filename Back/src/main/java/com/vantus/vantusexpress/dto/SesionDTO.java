package com.vantus.vantusexpress.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vantus.vantusexpress.entity.Sesion;

public class SesionDTO {

    @JsonProperty("call_sid")
    private String callSid;

    @JsonProperty("paso_actual")
    private String pasoActual;

    private Object datos;

    public String getCallSid() { return callSid; }
    public void setCallSid(String callSid) { this.callSid = callSid; }
    public String getPasoActual() { return pasoActual; }
    public void setPasoActual(String pasoActual) { this.pasoActual = pasoActual; }
    public Object getDatos() { return datos; }
    public void setDatos(Object datos) { this.datos = datos; }

    public Sesion toEntity() {
        Sesion sesion = new Sesion();
        sesion.setCallSid(this.callSid);
        sesion.setPasoActual(this.pasoActual);
        try {
            if (this.datos == null) {
                sesion.setDatos("{}");
            } else if (this.datos instanceof String) {
                sesion.setDatos((String) this.datos);
            } else {
                sesion.setDatos(new ObjectMapper().writeValueAsString(this.datos));
            }
        } catch (JsonProcessingException e) {
            sesion.setDatos("{}");
        }
        return sesion;
    }
}
