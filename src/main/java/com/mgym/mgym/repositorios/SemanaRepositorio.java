package com.mgym.mgym.repositorios;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mgym.mgym.entidades.Semana;

@Repository
public interface SemanaRepositorio extends JpaRepository<Semana, UUID>{
@Query("SELECT l FROM Semana WHERE l.semanaId = :semanaId")
    public Semana buscarSemanabyId(@Param("semanaId") UUID semanaId);
}
