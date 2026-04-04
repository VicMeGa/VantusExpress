package com.vantus.vantusexpress.repository;

import com.vantus.vantusexpress.entity.Destinatario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DestinatarioRepository extends JpaRepository<Destinatario, Integer> {
    List<Destinatario> findByClienteId(Integer clienteId);

    //@Query("SELECT d FROM Destinatario d WHERE " +
    //       "LOWER(d.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
    //       "LOWER(d.telefono) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
    //       "LOWER(d.direccion) LIKE LOWER(CONCAT('%', :q, '%'))")
    //Page<Destinatario> buscar(@Param("q") String q, Pageable pageable);
    @Query("SELECT d FROM Destinatario d JOIN d.cliente c WHERE " +
       "LOWER(d.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
       "LOWER(d.telefono) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
       "LOWER(d.direccion) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
       "CAST(d.cliente.id AS string) LIKE CONCAT('%', :q, '%')")
    Page<Destinatario> buscar(@Param("q") String q, Pageable pageable);

    Page<Destinatario> findAll(Pageable pageable);
}