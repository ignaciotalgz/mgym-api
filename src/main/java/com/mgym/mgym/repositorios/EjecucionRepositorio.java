package com.mgym.mgym.repositorios;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mgym.mgym.entidades.Ejecucion;
import com.mgym.mgym.enumeraciones.EjecucionTipo;

@Repository
public interface EjecucionRepositorio extends JpaRepository<Ejecucion, UUID>{
    @Query("SELECT l FROM Ejecucion l WHERE l.dificultad = :dificultad")
    public Ejecucion buscarEjecucionbyDificultad(@Param("dificultad") int dificultad);

    @Query("SELECT l FROM Ejecucion l WHERE l.tipo = :tipo")
    public Ejecucion buscarEjecucionbyTipo(@Param("tipo") EjecucionTipo tipo);
}
