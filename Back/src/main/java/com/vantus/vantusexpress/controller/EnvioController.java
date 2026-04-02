package com.vantus.vantusexpress.controller;

import com.vantus.vantusexpress.entity.Envio;
import com.vantus.vantusexpress.service.EnvioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vantus.vantusexpress.dto.ApiResponse;
import java.util.Map;

@RestController
@RequestMapping("/envios")
public class EnvioController {

    private final EnvioService envioService;

    public EnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }

    //@PostMapping
    //public ResponseEntity<Envio> crear(@RequestParam Integer clienteId,
    //                                    @RequestParam Integer destinatarioId,
    //                                    @RequestBody Envio envio) {
    //    try {
    //        return ResponseEntity.status(HttpStatus.CREATED)
    //                .body(envioService.crear(clienteId, destinatarioId, envio));
    //    } catch (RuntimeException e) {
    //        return ResponseEntity.notFound().build();
    //    }
    //}
    @PostMapping
    public ResponseEntity<ApiResponse> crear(
            @RequestParam(required = false) Integer clienteId,
            @RequestParam(required = false) Integer destinatarioId,
            @RequestBody(required = false) Envio envio) {
        try {
            if (clienteId == null || destinatarioId == null)
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("clienteId y destinatarioId son obligatorios", null));
            if (envio == null || envio.getContenido() == null)
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("contenido es obligatorio", null));
            Envio nuevo = envioService.crear(clienteId, destinatarioId, envio);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Envío creado exitosamente", nuevo));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        }
    }
/* 
    @GetMapping
    public ResponseEntity<Envio> obtenerPorFolio(@RequestParam String folio) {
        return envioService.obtenerPorFolio(folio)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
*/
    //@GetMapping
    //public ResponseEntity<?> obtener(@RequestParam(required = false) String folio) {
    //    if (folio != null) {
    //        return envioService.obtenerPorFolio(folio)
    //                .map(ResponseEntity::ok)
    //                .orElse(ResponseEntity.notFound().build());
    //    }
    //    return ResponseEntity.ok(envioService.obtenerTodos());
    //}
    //@GetMapping("/{id}")
    //public ResponseEntity<Envio> obtenerPorId(@PathVariable Integer id) {
    //    return envioService.obtenerPorId(id)
    //            .map(ResponseEntity::ok)
    //            .orElse(ResponseEntity.notFound().build());
    //}
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
            var resultado = envioService.obtener(id, page, pageSize, q);
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
    //public ResponseEntity<Envio> actualizar(@PathVariable Integer id,
    //                                         @RequestBody Envio envio) {
    //    try {
    //        return ResponseEntity.ok(envioService.actualizar(id, envio));
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
            int actualizados = envioService.actualizar(id, campos);
            return ResponseEntity.ok(ApiResponse.ok("Envío actualizado", actualizados));
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
            int eliminados = envioService.eliminar(id);
            return ResponseEntity.ok(ApiResponse.ok("Envío eliminado", eliminados));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        }
    }
}