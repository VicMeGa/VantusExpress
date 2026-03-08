package com.vantus.vantusexpress.repository;

import com.vantus.vantusexpress.entity.Destinatario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DestinatarioRepository extends JpaRepository<Destinatario, Integer> {
    List<Destinatario> findByClienteId(Integer clienteId);
}