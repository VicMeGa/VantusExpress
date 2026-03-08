package com.vantus.vantusexpress.service;

import com.vantus.vantusexpress.entity.Sesion;
import com.vantus.vantusexpress.repository.SesionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class SesionService {

    private final SesionRepository sesionRepository;

    public SesionService(SesionRepository sesionRepository) {
        this.sesionRepository = sesionRepository;
    }

    public Sesion crear(Sesion sesion) {
        return sesionRepository.save(sesion);
    }

    public Optional<Sesion> obtenerPorCallSid(String callSid) {
        return sesionRepository.findByCallSid(callSid);
    }

    public Sesion actualizar(String callSid, Sesion datos) {
        Sesion sesion = sesionRepository.findByCallSid(callSid)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada: " + callSid));
        sesion.setPasoActual(datos.getPasoActual());
        sesion.setDatos(datos.getDatos());
        return sesionRepository.save(sesion);
    }

    @Transactional
    public void eliminar(String callSid) {
        sesionRepository.deleteByCallSid(callSid);
    }
}