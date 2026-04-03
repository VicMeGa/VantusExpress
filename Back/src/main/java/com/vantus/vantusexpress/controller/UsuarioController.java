package com.vantus.vantusexpress.controller;

import com.vantus.vantusexpress.entity.Usuario;
import com.vantus.vantusexpress.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import com.vantus.vantusexpress.dto.ApiResponse;

@RestController
@RequestMapping("/auth")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioService.registrar(usuario);
            nuevo.setPassword(null); // no devolver el hash
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    //@PostMapping("/login")
    //public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
    //    return usuarioService.login(body.get("email"), body.get("password"))
    //            .map(u -> {
    //                u.setPassword(null);
    //                return ResponseEntity.ok((Object) u);
    //            })
    //            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
    //                    .body(Map.of("error", "Email o contraseña incorrectos")));
    //}

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(
            @RequestBody(required = false) Map<String, String> body) {
        try {
            if (body == null || body.get("email") == null || body.get("password") == null)
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("email y password son obligatorios", null));
            return usuarioService.login(body.get("email"), body.get("password"))
                    .map(u -> {
                        u.setPassword(null);
                        return ResponseEntity.ok(ApiResponse.ok("Login exitoso", u));
                    })
                    .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(ApiResponse.error("Email o contraseña incorrectos", null)));
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
            var resultado = usuarioService.obtener(id, page, pageSize, q);
            resultado.getContent().forEach(u -> u.setPassword(null));
            return ResponseEntity.ok(ApiResponse.ok("OK", resultado.getContent()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> actualizar(
            @PathVariable Integer id,
            @RequestBody(required = false) Map<String, Object> campos) {
        try {
            if (campos == null || campos.isEmpty())
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Se debe enviar al menos un campo a actualizar", null));
            int actualizados = usuarioService.actualizar(id, campos);
            return ResponseEntity.ok(ApiResponse.ok("Usuario actualizado", actualizados));
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
            int eliminados = usuarioService.eliminar(id);
            return ResponseEntity.ok(ApiResponse.ok("Usuario eliminado", eliminados));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(e.getMessage(), e.getStackTrace()));
        }
    }
}