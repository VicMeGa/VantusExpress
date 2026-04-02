package com.vantus.vantusexpress.service;

import com.vantus.vantusexpress.entity.Cliente;
import com.vantus.vantusexpress.entity.Destinatario;
import com.vantus.vantusexpress.entity.Envio;
import com.vantus.vantusexpress.repository.ClienteRepository;
import com.vantus.vantusexpress.repository.DestinatarioRepository;
import com.vantus.vantusexpress.repository.EnvioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final ClienteRepository clienteRepository;
    private final DestinatarioRepository destinatarioRepository;

    public EnvioService(EnvioRepository envioRepository,
                        ClienteRepository clienteRepository,
                        DestinatarioRepository destinatarioRepository) {
        this.envioRepository = envioRepository;
        this.clienteRepository = clienteRepository;
        this.destinatarioRepository = destinatarioRepository;
    }

    //Psible servicio dependiendo
    private String generarFolio() {
        return "PX" + System.currentTimeMillis();
    }

    public Envio crear(Integer clienteId, Integer destinatarioId, Envio envio) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + clienteId));
        Destinatario destinatario = destinatarioRepository.findById(destinatarioId)
                .orElseThrow(() -> new RuntimeException("Destinatario no encontrado: " + destinatarioId));
        envio.setCliente(cliente);
        envio.setDestinatario(destinatario);
        envio.setFolio(generarFolio());
        return envioRepository.save(envio);
    }
    ////////////////////////////////////
    //public Optional<Envio> obtenerPorFolio(String folio) {
    //    return envioRepository.findByFolio(folio);
    //}
//
    //public Optional<Envio> obtenerPorId(Integer id) {
    //    return envioRepository.findById(id);
    //}
    
    public Page<Envio> obtener(Integer id, int page, int pageSize, String q) {
        if (id != null) {
            Envio e = envioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Envío no encontrado: " + id));
            return new org.springframework.data.domain.PageImpl<>(List.of(e));
        }
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        if (q != null && !q.isBlank()) {
            return envioRepository.buscar(q, pageable);
        }
        return envioRepository.findAll(pageable);
    }
    /////////////////////////////////////

    //public Envio actualizar(Integer id, Envio datos) {
    //    Envio envio = envioRepository.findById(id)
    //            .orElseThrow(() -> new RuntimeException("Envío no encontrado: " + id));
    //    envio.setContenido(datos.getContenido());
    //    envio.setValorEstimado(datos.getValorEstimado());
    //    envio.setEstado(datos.getEstado());
    //    return envioRepository.save(envio);
    //}
    public int actualizar(Integer id, Map<String, Object> campos) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado: " + id));
        if (campos.containsKey("contenido"))
            envio.setContenido((String) campos.get("contenido"));
        if (campos.containsKey("estado"))
            envio.setEstado((String) campos.get("estado"));
        if (campos.containsKey("valorEstimado"))
            envio.setValorEstimado(new BigDecimal(campos.get("valorEstimado").toString()));
        envioRepository.save(envio);
        return 1;
    }

    public int eliminar(Integer id) {
        if (!envioRepository.existsById(id))
            throw new RuntimeException("Envío no encontrado: " + id);
        envioRepository.deleteById(id);
        return 1;
    }

    public List<Envio> obtenerTodos() {
        return envioRepository.findAll();
    }
}