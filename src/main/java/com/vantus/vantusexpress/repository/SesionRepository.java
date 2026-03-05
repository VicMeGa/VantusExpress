package com.vantus.vantusexpress.repository;

import com.vantus.vantusexpress.entity.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SesionRepository extends JpaRepository<Sesion, Integer> {
    Optional<Sesion> findByCallSid(String callSid);
    void deleteByCallSid(String callSid);
}