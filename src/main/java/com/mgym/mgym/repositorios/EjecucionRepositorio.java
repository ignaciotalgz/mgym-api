package com.mgym.mgym.repositorios;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mgym.mgym.entidades.Dia;
import com.mgym.mgym.entidades.Ejecucion;
import com.mgym.mgym.entidades.Ejercicio;
import com.mgym.mgym.enumeraciones.EjecucionTipo;

@Repository
public interface EjecucionRepositorio extends JpaRepository<Ejecucion, UUID>{
    // ── Por día ────────────────────────────────────────────────────────────────
 
    /**
     * Todas las ejecuciones de un día.
     * Esto da la "sesión completa" de entrenamiento de ese día.
     */
    List<Ejecucion> findByDia(Dia dia);
 
    /**
     * Ejecuciones de un día ordenadas por ejercicio.
     * Útil para mostrar la sesión de forma organizada.
     */
    List<Ejecucion> findByDiaOrderByEjercicioNombreAsc(Dia dia);
 
    // ── Por ejercicio ──────────────────────────────────────────────────────────
 
    /**
     * Todo el historial de ejecuciones de un ejercicio específico.
     * Base para calcular la progresión: cuánto peso se usó con el tiempo.
     */
    List<Ejecucion> findByEjercicio(Ejercicio ejercicio);
 
    /**
     * Historial de un ejercicio ordenado por fecha del día (ascendente).
     * La forma más natural de ver la progresión cronológica.
     */
    List<Ejecucion> findByEjercicioOrderByDiaDiaAsc(Ejercicio ejercicio);
 
    /**
     * Historial de un ejercicio ordenado por fecha del día (descendente).
     * Para ver primero los más recientes.
     */
    List<Ejecucion> findByEjercicioOrderByDiaDiaDesc(Ejercicio ejercicio);
 
    // ── Por tipo de ejecución ──────────────────────────────────────────────────
 
    /** Ejecuciones de un día filtradas por tipo (Normal, RestPause, DropSet). */
    List<Ejecucion> findByDiaAndTipo(Dia dia, EjecucionTipo tipo);
 
    /** Historial de un ejercicio filtrado por tipo. */
    List<Ejecucion> findByEjercicioAndTipo(Ejercicio ejercicio, EjecucionTipo tipo);
 
    // ── Progresión y auditoría ─────────────────────────────────────────────────
 
    /**
     * Ejecuciones completadas (con repeticiones logradas > 0) de un ejercicio.
     * Separa lo "planificado" de lo "efectivamente ejecutado".
     */
    List<Ejecucion> findByEjercicioAndRepeticionesLogradasGreaterThan(Ejercicio ejercicio, int cero);
 
    /**
     * Ejecuciones pendientes de completar en un día
     * (planificadas pero sin repeticiones logradas aún).
     */
    List<Ejecucion> findByDiaAndRepeticionesLogradas(Dia dia, int cero);
 
    /**
     * Ejecuciones de un ejercicio donde el peso supera un umbral.
     * Útil para detectar récords personales o sesiones intensas.
     */
    List<Ejecucion> findByEjercicioAndPesoGreaterThanEqual(Ejercicio ejercicio, float pesoMinimo);
 
    /**
     * La ejecución de mayor peso registrada para un ejercicio.
     * → Récord personal de ese ejercicio.
     */
    @Query("SELECT e FROM Ejecucion e WHERE e.ejercicio = :ejercicio ORDER BY e.peso DESC LIMIT 1")
    java.util.Optional<Ejecucion> findMaxPesoByEjercicio(@Param("ejercicio") Ejercicio ejercicio);
 
    /**
     * Todas las ejecuciones de un ejercicio dentro de un día específico.
     * Un mismo ejercicio puede aparecer varias veces en el mismo día
     * (ej: series de calentamiento + series de trabajo).
     */
    List<Ejecucion> findByEjercicioAndDia(Ejercicio ejercicio, Dia dia);
 
    // ── Conteos ────────────────────────────────────────────────────────────────
 
    /** Cantidad de ejecuciones planificadas para un día. */
    int countByDia(Dia dia);
 
    /** Cuántas veces se ha ejecutado un ejercicio en total. */
    int countByEjercicio(Ejercicio ejercicio);
 
    /**
     * Cantidad de veces que se realizó un ejercicio en un día
     * (para detectar si ya fue planificado antes de agregar otro).
     */
    int countByEjercicioAndDia(Ejercicio ejercicio, Dia dia);
}
