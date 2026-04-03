package com.vantus.vantusexpress.controller;

import com.vantus.vantusexpress.dto.SesionDTO;
import com.vantus.vantusexpress.entity.Sesion;
import com.vantus.vantusexpress.service.SesionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vantus.vantusexpress.dto.ApiResponse;

import java.util.Map;

@RestController
@RequestMapping("/sesion")
public class SesionController {

    private final SesionService sesionService;

    public SesionController(SesionService sesionService) {
        this.sesionService = sesionService;
    }

    //@PostMapping
    //public ResponseEntity<Sesion> crear(@RequestBody SesionDTO dto) {
    //    return ResponseEntity.status(HttpStatus.CREATED)
    //            .body(sesionService.crear(dto.toEntity()));
    //}

    @PostMapping
    public ResponseEntity<ApiResponse> crear(@RequestBody(required = false) Sesion sesion) {
        try {
            if (sesion == null || sesion.getCallSid() == null)
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("callSid es obligatorio", null));
            Sesion nueva = sesionService.crear(sesion);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Sesión creada exitosamente", nueva));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        }
    }

    //@GetMapping
    //public ResponseEntity<?> obtenerTodos() {
    //    return ResponseEntity.ok(sesionService.obtenerTodos());
    //}
//
    //@GetMapping("/{callSid}")
    //public ResponseEntity<Sesion> obtenerPorCallSid(@PathVariable String callSid) {
    //    return sesionService.obtenerPorCallSid(callSid)
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
            var resultado = sesionService.obtener(id, page, pageSize, q);
            return ResponseEntity.ok(ApiResponse.ok("OK", resultado.getContent()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        }
    }

    @GetMapping("/{callSid}")
    public ResponseEntity<ApiResponse> obtenerPorCallSid(@PathVariable String callSid) {
        try {
            return sesionService.obtenerPorCallSid(callSid)
                    .map(s -> ResponseEntity.ok(ApiResponse.ok("OK", s)))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.error("Sesión no encontrada: " + callSid, null)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        }
    }

    //@PutMapping("/{callSid}")
    //public ResponseEntity<Sesion> actualizar(@PathVariable String callSid,
    //                                          @RequestBody SesionDTO dto) {
    //    try {
    //        return ResponseEntity.ok(sesionService.actualizar(callSid, dto.toEntity()));
    //    } catch (RuntimeException e) {
    //        return ResponseEntity.notFound().build();
    //    }
    //}

    @PutMapping("/{callSid}")
    public ResponseEntity<ApiResponse> actualizar(
            @PathVariable String callSid,
            @RequestBody(required = false) Map<String, Object> campos) {
        try {
            if (campos == null || campos.isEmpty())
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Se debe enviar al menos un campo a actualizar", null));

            Sesion sesion = sesionService.obtenerPorCallSid(callSid)
                    .orElseThrow(() -> new RuntimeException("Sesión no encontrada: " + callSid));
            int actualizados = sesionService.actualizar(sesion.getId(), campos);
            return ResponseEntity.ok(ApiResponse.ok("Sesión actualizada", actualizados));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        }
    }

    @DeleteMapping("/{callSid}")
    public ResponseEntity<ApiResponse> eliminar(@PathVariable String callSid) {
        try {
            int eliminados = sesionService.eliminarPorCallSid(callSid);
            return ResponseEntity.ok(ApiResponse.ok("Sesión eliminada", eliminados));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        }
    }
}
