package com.vantus.vantusexpress.service;

import com.vantus.vantusexpress.entity.Cliente;
import com.vantus.vantusexpress.entity.Destinatario;
import com.vantus.vantusexpress.repository.ClienteRepository;
import com.vantus.vantusexpress.repository.DestinatarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Map;

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

    //public List<Destinatario> obtenerPorClienteId(Integer clienteId) {
    //    return destinatarioRepository.findByClienteId(clienteId);
    //}
//
    //public Optional<Destinatario> obtenerPorId(Integer id) {
    //    return destinatarioRepository.findById(id);
    //}

    public Page<Destinatario> obtener(Integer id, Integer clienteId, int page, int pageSize, String q) {
        if (id != null) {
            Destinatario d = destinatarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Destinatario no encontrado: " + id));
            return new PageImpl<>(List.of(d));
        }
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        if (clienteId != null) {
            return new PageImpl<>(destinatarioRepository.findByClienteId(clienteId));
        }
        if (q != null && !q.isBlank()) {
            return destinatarioRepository.buscar(q, pageable);
        }
        return destinatarioRepository.findAll(pageable);
    }

    //public Destinatario actualizar(Integer id, Destinatario datos) {
    //    Destinatario destinatario = destinatarioRepository.findById(id)
    //            .orElseThrow(() -> new RuntimeException("Destinatario no encontrado: " + id));
    //    destinatario.setNombre(datos.getNombre());
    //    destinatario.setTelefono(datos.getTelefono());
    //    destinatario.setDireccion(datos.getDireccion());
    //    return destinatarioRepository.save(destinatario);
    //}

    public int actualizar(Integer id, Map<String, Object> campos) {
        Destinatario dest = destinatarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destinatario no encontrado: " + id));
        if (campos.containsKey("nombre"))    dest.setNombre((String) campos.get("nombre"));
        if (campos.containsKey("telefono"))  dest.setTelefono((String) campos.get("telefono"));
        if (campos.containsKey("direccion")) dest.setDireccion((String) campos.get("direccion"));
        destinatarioRepository.save(dest);
        return 1;
    }

    //public void eliminar(Integer id) {
    //    destinatarioRepository.deleteById(id);
    //}
    public int eliminar(Integer id) {
        if (!destinatarioRepository.existsById(id))
            throw new RuntimeException("Destinatario no encontrado: " + id);
        destinatarioRepository.deleteById(id);
        return 1;
    }
}