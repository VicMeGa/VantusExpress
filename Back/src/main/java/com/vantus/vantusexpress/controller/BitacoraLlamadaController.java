package com.vantus.vantusexpress.controller;

import com.vantus.vantusexpress.entity.BitacoraLlamada;
import com.vantus.vantusexpress.service.BitacoraLlamadaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vantus.vantusexpress.dto.ApiResponse;

@RestController
@RequestMapping("/bitacora")
public class BitacoraLlamadaController {

    private final BitacoraLlamadaService bitacoraService;

    public BitacoraLlamadaController(BitacoraLlamadaService bitacoraService) {
        this.bitacoraService = bitacoraService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> registrar(
            @RequestBody(required = false) BitacoraLlamada bitacora) {
        try {
            if (bitacora == null || bitacora.getCallSid() == null || bitacora.getTelefono() == null)
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("callSid y telefono son obligatorios", null));
            BitacoraLlamada nueva = bitacoraService.registrar(bitacora);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Llamada registrada exitosamente", nueva));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> obtener(
            @RequestParam(required = false) Integer id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(name = "page_size", defaultValue = "10") int pageSize,
            @RequestParam(required = false) String q) {
        try {
            if (page < 1 || pageSize < 1)
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("page y page_size deben ser mayores a 0", null));
            var resultado = bitacoraService.obtener(id, page, pageSize, q);
            return ResponseEntity.ok(ApiResponse.ok("OK", resultado.getContent()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> eliminar(@PathVariable Integer id) {
        try {
            int eliminados = bitacoraService.eliminar(id);
            return ResponseEntity.ok(ApiResponse.ok("Registro eliminado", eliminados));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        }
    }
}