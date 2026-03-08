package com.vantus.vantusexpress.controller;

import com.vantus.vantusexpress.entity.BitacoraLlamada;
import com.vantus.vantusexpress.service.BitacoraLlamadaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bitacora")
public class BitacoraLlamadaController {

    private final BitacoraLlamadaService bitacoraService;

    public BitacoraLlamadaController(BitacoraLlamadaService bitacoraService) {
        this.bitacoraService = bitacoraService;
    }

    @PostMapping
    public ResponseEntity<BitacoraLlamada> registrar(@RequestBody BitacoraLlamada bitacora) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bitacoraService.registrar(bitacora));
    }
}