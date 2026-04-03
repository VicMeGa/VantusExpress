package com.vantus.vantusexpress.repository;

import com.vantus.vantusexpress.entity.BitacoraLlamada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BitacoraLlamadaRepository extends JpaRepository<BitacoraLlamada, Integer> {
    @Query("SELECT b FROM BitacoraLlamada b WHERE " +
           "LOWER(b.callSid) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(b.telefono) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(b.resultado) LIKE LOWER(CONCAT('%', :q, '%'))")
    Page<BitacoraLlamada> buscar(@Param("q") String q, Pageable pageable);

    Page<BitacoraLlamada> findAll(Pageable pageable);
}