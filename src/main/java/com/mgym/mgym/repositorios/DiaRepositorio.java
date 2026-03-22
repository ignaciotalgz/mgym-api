package com.mgym.mgym.repositorios;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mgym.mgym.entidades.Dia;

@Repository
public interface DiaRepositorio extends JpaRepository<Dia, UUID>{
    @Query("SELECT l FROM Dia l WHERE l.dia = :dia")
    public Dia buscarDiabyFecha(@Param("dia") Date dia);
}
