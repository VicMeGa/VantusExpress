package com.vantus.vantusexpress.controller;

import com.vantus.vantusexpress.entity.Destinatario;
import com.vantus.vantusexpress.service.DestinatarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/destinatarios")
public class DestinatarioController {

    private final DestinatarioService destinatarioService;

    public DestinatarioController(DestinatarioService destinatarioService) {
        this.destinatarioService = destinatarioService;
    }

    @PostMapping
    public ResponseEntity<Destinatario> crear(@RequestParam Integer clienteId,
                                               @RequestBody Destinatario destinatario) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(destinatarioService.crear(clienteId, destinatario));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Destinatario>> obtenerPorClienteId(@RequestParam Integer clienteId) {
        return ResponseEntity.ok(destinatarioService.obtenerPorClienteId(clienteId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Destinatario> obtenerPorId(@PathVariable Integer id) {
        return destinatarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Destinatario> actualizar(@PathVariable Integer id,
                                                    @RequestBody Destinatario destinatario) {
        try {
            return ResponseEntity.ok(destinatarioService.actualizar(id, destinatario));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        destinatarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}