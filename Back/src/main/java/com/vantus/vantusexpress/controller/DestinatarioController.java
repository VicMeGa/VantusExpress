package com.vantus.vantusexpress.controller;

import com.vantus.vantusexpress.entity.Destinatario;
import com.vantus.vantusexpress.service.DestinatarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.vantus.vantusexpress.dto.ApiResponse;
import java.util.Map;

@RestController
@RequestMapping("/destinatarios")
public class DestinatarioController {

    private final DestinatarioService destinatarioService;

    public DestinatarioController(DestinatarioService destinatarioService) {
        this.destinatarioService = destinatarioService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> crear(
            @RequestParam(required = false) Integer clienteId,
            @RequestBody(required = false) Destinatario destinatario) {
        try {
            if (clienteId == null)
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("clienteId es obligatorio", null));
            if (destinatario == null || destinatario.getNombre() == null || destinatario.getTelefono() == null)
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("nombre y telefono son obligatorios", null));
            Destinatario nuevo = destinatarioService.crear(clienteId, destinatario);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Destinatario creado exitosamente", nuevo));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        }
    }

    //@GetMapping
    //public ResponseEntity<List<Destinatario>> obtenerPorClienteId(@RequestParam Integer clienteId) {
    //    return ResponseEntity.ok(destinatarioService.obtenerPorClienteId(clienteId));
    //}
//
    //@GetMapping("/{id}")
    //public ResponseEntity<Destinatario> obtenerPorId(@PathVariable Integer id) {
    //    return destinatarioService.obtenerPorId(id)
    //            .map(ResponseEntity::ok)
    //            .orElse(ResponseEntity.notFound().build());
    //}

    @GetMapping
    public ResponseEntity<ApiResponse> obtener(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer clienteId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(name = "page_size", defaultValue = "10") int pageSize,
            @RequestParam(required = false) String q) {
        try {
            if (page < 1 || pageSize < 1)
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("page y page_size deben ser mayores a 0", null));
            var resultado = destinatarioService.obtener(id, clienteId, page, pageSize, q);
            return ResponseEntity.ok(ApiResponse.ok("OK", resultado.getContent()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        }
    }

    //@PutMapping("/{id}")
    //public ResponseEntity<Destinatario> actualizar(@PathVariable Integer id,
    //                                                @RequestBody Destinatario destinatario) {
    //    try {
    //        return ResponseEntity.ok(destinatarioService.actualizar(id, destinatario));
    //    } catch (RuntimeException e) {
    //        return ResponseEntity.notFound().build();
    //    }
    //}

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody(required = false) Map<String, Object> campos) {
        try {
            if (campos == null || campos.isEmpty())
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Se debe enviar al menos un campo a actualizar", null));
            int actualizados = destinatarioService.actualizar(id, campos);
            return ResponseEntity.ok(ApiResponse.ok("Destinatario actualizado", actualizados));
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
            int eliminados = destinatarioService.eliminar(id);
            return ResponseEntity.ok(ApiResponse.ok("Destinatario eliminado", eliminados));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        }
    }
}