package com.mgym.mgym.repositorios;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mgym.mgym.entidades.Dia;
import com.mgym.mgym.entidades.Semana;

@Repository
public interface DiaRepositorio extends JpaRepository<Dia, UUID>{
    // ── Por semana ─────────────────────────────────────────────────────────────
 
    /** Todos los días de una semana. */
    List<Dia> findBySemana(Semana semana);
 
    /** Días de una semana ordenados por fecha. */
    List<Dia> findBySemanaOrderByDiaAsc(Semana semana);
 
    /** Días de una semana ordenados por nombre. */
    List<Dia> findBySemanaOrderByNombreAsc(Semana semana);
 
    // ── Búsqueda por nombre ────────────────────────────────────────────────────
 
    /** Buscar un día por nombre exacto dentro de una semana. */
    Optional<Dia> findBySemanaAndNombre(Semana semana, String nombre);
 
    // ── Búsqueda por fecha ─────────────────────────────────────────────────────
 
    /**
     * Buscar el día que corresponde a una fecha real.
     * Útil para saber "qué entrenamiento hice el día X".
     */
    Optional<Dia> findByDia(Date fecha);
 
    /**
     * Días entrenados en un rango de fechas.
     * Útil para mostrar el historial de un período.
     */
    List<Dia> findByDiaBetween(Date desde, Date hasta);
 
    // ── Conteos ────────────────────────────────────────────────────────────────
 
    /** Cantidad de días que tiene una semana. */
    int countBySemana(Semana semana);
 
    // ── Validaciones ───────────────────────────────────────────────────────────
 
    /** Verificar si ya existe un día con ese nombre en la semana. */
    boolean existsBySemanaAndNombreIgnoreCase(Semana semana, String nombre);
 
    /** Verificar si ya existe un día registrado en esa fecha. */
    boolean existsByDia(Date fecha);
}
