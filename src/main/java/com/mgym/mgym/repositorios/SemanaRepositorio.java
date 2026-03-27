package com.mgym.mgym.repositorios;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mgym.mgym.entidades.Rutina;
import com.mgym.mgym.entidades.Semana;

@Repository
public interface SemanaRepositorio extends JpaRepository<Semana, UUID>{
    // ── Por rutina ─────────────────────────────────────────────────────────────
 
    /**
     * Todas las semanas de una rutina, en orden de inserción.
     * Útil para mostrar la progresión completa de una rutina.
     */
    List<Semana> findByRutina(Rutina rutina);
 
    /**
     * Todas las semanas de una rutina ordenadas por nombre.
     * Conveniente si los nombres son "Semana 1", "Semana 2", etc.
     */
    List<Semana> findByRutinaOrderByNombreAsc(Rutina rutina);
 
    // ── Búsqueda por nombre ────────────────────────────────────────────────────
 
    /** Buscar una semana por nombre exacto dentro de una rutina. */
    Optional<Semana> findByRutinaAndNombre(Rutina rutina, String nombre);
 
    // ── Conteos ────────────────────────────────────────────────────────────────
 
    /** Cantidad de semanas que tiene una rutina. */
    int countByRutina(Rutina rutina);
 
    // ── Validaciones ───────────────────────────────────────────────────────────
 
    /** Verificar si ya existe una semana con ese nombre en la rutina dada. */
    boolean existsByRutinaAndNombreIgnoreCase(Rutina rutina, String nombre);
}
