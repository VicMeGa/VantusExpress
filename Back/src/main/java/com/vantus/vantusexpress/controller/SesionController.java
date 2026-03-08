package com.vantus.vantusexpress.controller;

import com.vantus.vantusexpress.entity.Sesion;
import com.vantus.vantusexpress.service.SesionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sesion")
public class SesionController {

    private final SesionService sesionService;

    public SesionController(SesionService sesionService) {
        this.sesionService = sesionService;
    }

    @PostMapping
    public ResponseEntity<Sesion> crear(@RequestBody Sesion sesion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sesionService.crear(sesion));
    }

    @GetMapping("/{callSid}")
    public ResponseEntity<Sesion> obtener(@PathVariable String callSid) {
        return sesionService.obtenerPorCallSid(callSid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{callSid}")
    public ResponseEntity<Sesion> actualizar(@PathVariable String callSid,
                                              @RequestBody Sesion sesion) {
        try {
            return ResponseEntity.ok(sesionService.actualizar(callSid, sesion));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{callSid}")
    public ResponseEntity<Void> eliminar(@PathVariable String callSid) {
        sesionService.eliminar(callSid);
        return ResponseEntity.noContent().build();
    }
}