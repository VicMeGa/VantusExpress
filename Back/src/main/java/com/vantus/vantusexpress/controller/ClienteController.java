package com.vantus.vantusexpress.controller;

import com.vantus.vantusexpress.entity.Cliente;
import com.vantus.vantusexpress.service.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.vantus.vantusexpress.dto.ApiResponse;
import java.util.Map;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    //@PostMapping
    //public ResponseEntity<Cliente> crear(@RequestBody Cliente cliente) {
    //    return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.crear(cliente));
    //}
    @PostMapping
    public ResponseEntity<ApiResponse> crear(@RequestBody(required = false) Cliente cliente) {
        try {
            if (cliente == null || cliente.getNombre() == null || cliente.getTelefono() == null)
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("nombre y telefono son obligatorios", null));
            Cliente nuevo = clienteService.crear(cliente);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.ok("Cliente creado exitosamente", nuevo));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        }
    }

    //@GetMapping
    //public ResponseEntity<List<Cliente>> obtenerPorTelefono(@RequestParam String telefono) {
    //    return ResponseEntity.ok(clienteService.obtenerPorTelefono(telefono));
    //}

    //@GetMapping
    //public ResponseEntity<List<Cliente>> obtener(@RequestParam(required = false) String telefono) {
    //    if (telefono != null && !telefono.isBlank()) {
    //        return ResponseEntity.ok(clienteService.obtenerPorTelefono(telefono));
    //    }
    //    return ResponseEntity.ok(clienteService.obtenerTodos());
    //}
//
    //@GetMapping("/{id}")
    //public ResponseEntity<Cliente> obtenerPorId(@PathVariable Integer id) {
    //    return clienteService.obtenerPorId(id)
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
            var resultado = clienteService.obtener(id, page, pageSize, q);
            return ResponseEntity.ok(ApiResponse.ok("OK", resultado.getContent()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        }
    }

    //@PutMapping("/{id}")
    //public ResponseEntity<Cliente> actualizar(@PathVariable Integer id,
    //                                           @RequestBody Cliente cliente) {
    //    try {
    //        return ResponseEntity.ok(clienteService.actualizar(id, cliente));
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
            int actualizados = clienteService.actualizar(id, campos);
            return ResponseEntity.ok(ApiResponse.ok("Cliente actualizado", actualizados));
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
            int eliminados = clienteService.eliminar(id);
            return ResponseEntity.ok(ApiResponse.ok("Cliente eliminado", eliminados));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        }
    }
}