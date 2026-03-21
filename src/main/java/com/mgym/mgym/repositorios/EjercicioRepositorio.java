package com.mgym.mgym.repositorios;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mgym.mgym.entidades.Ejercicio;

@Repository
public interface EjercicioRepositorio extends JpaRepository<Ejercicio, UUID>{
@Query("SELECT l FROM Ejercicio WHERE l.ejercicioId = :ejercicioId")
    public Ejercicio buscarEjerciciobyId(@Param("ejercicioId") UUID ejercicioId);
}
