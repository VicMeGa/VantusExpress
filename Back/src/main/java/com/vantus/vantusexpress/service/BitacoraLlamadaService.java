package com.vantus.vantusexpress.service;

import com.vantus.vantusexpress.entity.BitacoraLlamada;
import com.vantus.vantusexpress.repository.BitacoraLlamadaRepository;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;

@Service
public class BitacoraLlamadaService {

    private final BitacoraLlamadaRepository bitacoraRepository;

    public BitacoraLlamadaService(BitacoraLlamadaRepository bitacoraRepository) {
        this.bitacoraRepository = bitacoraRepository;
    }

    public BitacoraLlamada registrar(BitacoraLlamada bitacora) {
        return bitacoraRepository.save(bitacora);
    }

    public Page<BitacoraLlamada> obtener(Integer id, int page, int pageSize, String q) {
        if (id != null) {
            BitacoraLlamada b = bitacoraRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Registro no encontrado: " + id));
            return new PageImpl<>(List.of(b));
        }
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        if (q != null && !q.isBlank()) {
            return bitacoraRepository.buscar(q, pageable);
        }
        return bitacoraRepository.findAll(pageable);
    }

    public int eliminar(Integer id) {
        if (!bitacoraRepository.existsById(id))
            throw new RuntimeException("Registro no encontrado: " + id);
        bitacoraRepository.deleteById(id);
        return 1;
    }
}