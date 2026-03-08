package com.vantus.vantusexpress.service;

import com.vantus.vantusexpress.entity.Cliente;
import com.vantus.vantusexpress.entity.Destinatario;
import com.vantus.vantusexpress.repository.ClienteRepository;
import com.vantus.vantusexpress.repository.DestinatarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DestinatarioService {

    private final DestinatarioRepository destinatarioRepository;
    private final ClienteRepository clienteRepository;

    public DestinatarioService(DestinatarioRepository destinatarioRepository,
                               ClienteRepository clienteRepository) {
        this.destinatarioRepository = destinatarioRepository;
        this.clienteRepository = clienteRepository;
    }

    public Destinatario crear(Integer clienteId, Destinatario destinatario) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + clienteId));
        destinatario.setCliente(cliente);
        return destinatarioRepository.save(destinatario);
    }

    public List<Destinatario> obtenerPorClienteId(Integer clienteId) {
        return destinatarioRepository.findByClienteId(clienteId);
    }

    public Optional<Destinatario> obtenerPorId(Integer id) {
        return destinatarioRepository.findById(id);
    }

    public Destinatario actualizar(Integer id, Destinatario datos) {
        Destinatario destinatario = destinatarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destinatario no encontrado: " + id));
        destinatario.setNombre(datos.getNombre());
        destinatario.setTelefono(datos.getTelefono());
        destinatario.setDireccion(datos.getDireccion());
        return destinatarioRepository.save(destinatario);
    }

    public void eliminar(Integer id) {
        destinatarioRepository.deleteById(id);
    }
}