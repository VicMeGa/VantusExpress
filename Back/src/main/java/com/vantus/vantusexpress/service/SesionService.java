package com.vantus.vantusexpress.service;

import com.vantus.vantusexpress.entity.Sesion;
import com.vantus.vantusexpress.repository.SesionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Service
public class SesionService {

    private final SesionRepository sesionRepository;

    public SesionService(SesionRepository sesionRepository) {
        this.sesionRepository = sesionRepository;
    }

    public Sesion crear(Sesion sesion) {
        return sesionRepository.save(sesion);
    }

    public List<Sesion> obtenerTodos() {
        return sesionRepository.findAll();
    }

    public Optional<Sesion> obtenerPorCallSid(String callSid) {
        return sesionRepository.findByCallSid(callSid);
    }

    public Page<Sesion> obtener(Integer id, int page, int pageSize, String q) {
        if (id != null) {
            Sesion s = sesionRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Sesión no encontrada: " + id));
            return new PageImpl<>(List.of(s));
        }
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        if (q != null && !q.isBlank()) {
            return sesionRepository.buscar(q, pageable);
        }
        return sesionRepository.findAll(pageable);
    }

    //public Sesion actualizar(String callSid, Sesion datos) {
    //    Sesion sesion = sesionRepository.findByCallSid(callSid)
    //            .orElseThrow(() -> new RuntimeException("Sesión no encontrada: " + callSid));
    //    sesion.setPasoActual(datos.getPasoActual());
    //    sesion.setDatos(datos.getDatos());
    //    return sesionRepository.save(sesion);
    //}

   public int actualizar(Integer id, Map<String, Object> campos) {
        Sesion sesion = sesionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada: " + id));

        if (campos.containsKey("pasoActual"))
            sesion.setPasoActual((String) campos.get("pasoActual"));

        if (campos.containsKey("datos"))
            sesion.setDatos((String) campos.get("datos"));

        sesionRepository.save(sesion);
        return 1;
    }
    //public int eliminar(Integer id) {
    //    if (!sesionRepository.existsById(id))
    //        throw new RuntimeException("Sesión no encontrada: " + id);
    //    sesionRepository.deleteById(id);
    //    return 1;
    //}

    @Transactional
    public int eliminarPorCallSid(String callSid) {
        if (sesionRepository.findByCallSid(callSid).isEmpty())
            throw new RuntimeException("Sesión no encontrada: " + callSid);
        sesionRepository.deleteByCallSid(callSid);
        return 1;
    }
}