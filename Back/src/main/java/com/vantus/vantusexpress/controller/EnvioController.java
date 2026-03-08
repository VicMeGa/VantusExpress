package com.vantus.vantusexpress.controller;

import com.vantus.vantusexpress.entity.Envio;
import com.vantus.vantusexpress.service.EnvioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/envios")
public class EnvioController {

    private final EnvioService envioService;

    public EnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }

    @PostMapping
    public ResponseEntity<Envio> crear(@RequestParam Integer clienteId,
                                        @RequestParam Integer destinatarioId,
                                        @RequestBody Envio envio) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(envioService.crear(clienteId, destinatarioId, envio));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<Envio> obtenerPorFolio(@RequestParam String folio) {
        return envioService.obtenerPorFolio(folio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Envio> obtenerPorId(@PathVariable Integer id) {
        return envioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Envio> actualizar(@PathVariable Integer id,
                                             @RequestBody Envio envio) {
        try {
            return ResponseEntity.ok(envioService.actualizar(id, envio));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        envioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}