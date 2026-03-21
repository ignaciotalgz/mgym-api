package com.mgym.mgym.repositorios;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mgym.mgym.entidades.Ejecucion;

@Repository
public interface EjecucionRepositorio extends JpaRepository<Ejecucion, UUID>{
@Query("SELECT l FROM Ejecucion WHERE l.ejecucionId = :ejecucionId")
    public Ejecucion buscarEjecucionbyId(@Param("ejecucionId") UUID ejecucionId);
}
