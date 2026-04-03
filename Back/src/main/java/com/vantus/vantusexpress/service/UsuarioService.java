package com.vantus.vantusexpress.service;

import com.vantus.vantusexpress.entity.Usuario;
import com.vantus.vantusexpress.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        usuario.setPassword(encoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> login(String email, String password) {
        return usuarioRepository.findByEmail(email)
                .filter(u -> encoder.matches(password, u.getPassword()));
    }

    public Page<Usuario> obtener(Integer id, int page, int pageSize, String q) {
        if (id != null) {
            Usuario u = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
            return new PageImpl<>(List.of(u));
        }
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        if (q != null && !q.isBlank()) {
            return usuarioRepository.buscar(q, pageable);
        }
        return usuarioRepository.findAll(pageable);
    }

    public int actualizar(Integer id, Map<String, Object> campos) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + id));
        if (campos.containsKey("nombre"))
            usuario.setNombre((String) campos.get("nombre"));
        if (campos.containsKey("email"))
            usuario.setEmail((String) campos.get("email"));
        if (campos.containsKey("password"))
            usuario.setPassword(encoder.encode((String) campos.get("password")));
        usuarioRepository.save(usuario);
        return 1;
    }

    public int eliminar(Integer id) {
        if (!usuarioRepository.existsById(id))
            throw new RuntimeException("Usuario no encontrado: " + id);
        usuarioRepository.deleteById(id);
        return 1;
    }
}