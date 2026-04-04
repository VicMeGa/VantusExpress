package com.vantus.vantusexpress.repository;

import com.vantus.vantusexpress.entity.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EnvioRepository extends JpaRepository<Envio, Integer> {
    Optional<Envio> findByFolio(String folio);
    
    //@Query("SELECT e FROM Envio e WHERE " +
    //       "LOWER(e.folio) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
    //       "LOWER(e.contenido) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
    //       "LOWER(e.estado) LIKE LOWER(CONCAT('%', :q, '%'))")
    //Page<Envio> buscar(@Param("q") String q, Pageable pageable);
    @Query("SELECT e FROM Envio e JOIN e.cliente c JOIN e.destinatario d WHERE " +
       "LOWER(e.folio) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
       "LOWER(e.contenido) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
       "LOWER(e.estado) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
       "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
       "LOWER(d.nombre) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
       "LOWER(c.telefono) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
       "LOWER(d.telefono) LIKE LOWER(CONCAT('%', :q, '%'))")
    Page<Envio> buscar(@Param("q") String q, Pageable pageable);

    Page<Envio> findAll(Pageable pageable);
}