package com.vantus.vantusexpress.controller;

import com.vantus.vantusexpress.entity.Cliente;
import com.vantus.vantusexpress.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.crear(cliente));
    }

    //@GetMapping
    //public ResponseEntity<List<Cliente>> obtenerPorTelefono(@RequestParam String telefono) {
    //    return ResponseEntity.ok(clienteService.obtenerPorTelefono(telefono));
    //}

    @GetMapping
    public ResponseEntity<List<Cliente>> obtener(@RequestParam(required = false) String telefono) {
        if (telefono != null && !telefono.isBlank()) {
            return ResponseEntity.ok(clienteService.obtenerPorTelefono(telefono));
        }
        return ResponseEntity.ok(clienteService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Integer id) {
        return clienteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(@PathVariable Integer id,
                                               @RequestBody Cliente cliente) {
        try {
            return ResponseEntity.ok(clienteService.actualizar(id, cliente));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}