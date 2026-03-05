package com.vantus.vantusexpress.repository;

import com.vantus.vantusexpress.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    List<Cliente> findByTelefono(String telefono);
}