package com.vantus.vantusexpress.service;

import com.vantus.vantusexpress.entity.BitacoraLlamada;
import com.vantus.vantusexpress.repository.BitacoraLlamadaRepository;
import org.springframework.stereotype.Service;

@Service
public class BitacoraLlamadaService {

    private final BitacoraLlamadaRepository bitacoraRepository;

    public BitacoraLlamadaService(BitacoraLlamadaRepository bitacoraRepository) {
        this.bitacoraRepository = bitacoraRepository;
    }

    public BitacoraLlamada registrar(BitacoraLlamada bitacora) {
        return bitacoraRepository.save(bitacora);
    }
}