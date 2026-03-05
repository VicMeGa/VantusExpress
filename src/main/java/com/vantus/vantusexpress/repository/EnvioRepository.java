package com.vantus.vantusexpress.repository;

import com.vantus.vantusexpress.entity.Envio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EnvioRepository extends JpaRepository<Envio, Integer> {
    Optional<Envio> findByFolio(String folio);
}