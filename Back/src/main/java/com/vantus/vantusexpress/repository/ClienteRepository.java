package com.vantus.vantusexpress.repository;

import com.vantus.vantusexpress.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    List<Cliente> findByTelefono(String telefono);

    @Query("SELECT c FROM Cliente c WHERE " +
           "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(c.telefono) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(c.direccion) LIKE LOWER(CONCAT('%', :q, '%'))")
    Page<Cliente> buscar(@Param("q") String q, Pageable pageable);

    Page<Cliente> findAll(Pageable pageable);
}