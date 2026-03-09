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

    public Optional<Envio> obtenerPorFolio(String folio) {
        return envioRepository.findByFolio(folio);
    }

    public Optional<Envio> obtenerPorId(Integer id) {
        return envioRepository.findById(id);
    }

    public Envio actualizar(Integer id, Envio datos) {
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Envío no encontrado: " + id));
        envio.setContenido(datos.getContenido());
        envio.setValorEstimado(datos.getValorEstimado());
        envio.setEstado(datos.getEstado());
        return envioRepository.save(envio);
    }

    public void eliminar(Integer id) {
        envioRepository.deleteById(id);
    }

    public List<Envio> obtenerTodos() {
        return envioRepository.findAll();
    }
}