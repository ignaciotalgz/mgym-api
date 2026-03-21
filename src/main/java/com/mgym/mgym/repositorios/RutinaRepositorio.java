package com.mgym.mgym.repositorios;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mgym.mgym.entidades.Rutina;

@Repository
public interface RutinaRepositorio extends JpaRepository<Rutina, UUID>{
    @Query("SELECT l FROM Rutina WHERE l.nombre = :nombre")
    public Rutina buscarRutinabyNombre(@Param("nombre") UUID nombre);
}
