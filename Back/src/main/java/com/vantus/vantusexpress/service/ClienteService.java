package com.vantus.vantusexpress.service;

import com.vantus.vantusexpress.entity.Cliente;
import com.vantus.vantusexpress.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Map;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente crear(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    //public List<Cliente> obtenerTodos() {
    //    return clienteRepository.findAll();
    //}
    //
    //public List<Cliente> obtenerPorTelefono(String telefono) {
    //    return clienteRepository.findByTelefono(telefono);
    //}
//
    //public Optional<Cliente> obtenerPorId(Integer id) {
    //    return clienteRepository.findById(id);
    //}

    public Page<Cliente> obtener(Integer id, int page, int pageSize, String q) {
        if (id != null) {
            Cliente c = clienteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));
            return new org.springframework.data.domain.PageImpl<>(java.util.List.of(c));
        }
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        if (q != null && !q.isBlank()) {
            return clienteRepository.buscar(q, pageable);
        }
        return clienteRepository.findAll(pageable);
    }

    //public Cliente actualizar(Integer id, Cliente datos) {
    //    Cliente cliente = clienteRepository.findById(id)
    //            .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));
    //    cliente.setNombre(datos.getNombre());
    //    cliente.setTelefono(datos.getTelefono());
    //    cliente.setDireccion(datos.getDireccion());
    //    return clienteRepository.save(cliente);
    //}

    public int actualizar(Integer id, Map<String, Object> campos) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));
        if (campos.containsKey("nombre"))    cliente.setNombre((String) campos.get("nombre"));
        if (campos.containsKey("telefono"))  cliente.setTelefono((String) campos.get("telefono"));
        if (campos.containsKey("direccion")) cliente.setDireccion((String) campos.get("direccion"));
        clienteRepository.save(cliente);
        return 1;
    }

    //public void eliminar(Integer id) {
    //    clienteRepository.deleteById(id);
    //}

    public int eliminar(Integer id) {
        if (!clienteRepository.existsById(id))
            throw new RuntimeException("Cliente no encontrado: " + id);
        clienteRepository.deleteById(id);
        return 1;
    }
}