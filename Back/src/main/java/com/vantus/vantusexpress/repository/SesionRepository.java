package com.vantus.vantusexpress.repository;

import com.vantus.vantusexpress.entity.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SesionRepository extends JpaRepository<Sesion, Integer> {
    Optional<Sesion> findByCallSid(String callSid);
    void deleteByCallSid(String callSid);

    @Query("SELECT s FROM Sesion s WHERE " +
           "LOWER(s.callSid) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
           "LOWER(s.pasoActual) LIKE LOWER(CONCAT('%', :q, '%'))")
    Page<Sesion> buscar(@Param("q") String q, Pageable pageable);

    Page<Sesion> findAll(Pageable pageable);
}