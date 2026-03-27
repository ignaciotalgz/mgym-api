package com.mgym.mgym.repositorios;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mgym.mgym.entidades.Ejercicio;

@Repository
public interface EjercicioRepositorio extends JpaRepository<Ejercicio, UUID>{
     // ── Búsquedas básicas ──────────────────────────────────────────────────────
 
    /** Todos los ejercicios activos (no dados de baja). */
    List<Ejercicio> findByBajaFalse();
 
    /** Todos los ejercicios dados de baja (útil para auditoría). */
    List<Ejercicio> findByBajaTrue();
 
    /** Buscar por nombre exacto (case-sensitive). */
    Optional<Ejercicio> findByNombre(String nombre);
 
    /** Buscar por nombre ignorando mayúsculas/minúsculas. */
    Optional<Ejercicio> findByNombreIgnoreCase(String nombre);
 
    /** Buscar ejercicios cuyo nombre contenga el texto dado (búsqueda parcial). */
    List<Ejercicio> findByNombreContainingIgnoreCase(String texto);
 
    /** Buscar por nombre exacto solo entre los activos. */
    Optional<Ejercicio> findByNombreIgnoreCaseAndBajaFalse(String nombre);
 
    // ── Validaciones ───────────────────────────────────────────────────────────
 
    /** Verificar si ya existe un ejercicio con ese nombre (para evitar duplicados). */
    boolean existsByNombreIgnoreCase(String nombre);
 
    /** Verificar si existe un ejercicio activo con ese nombre. */
    boolean existsByNombreIgnoreCaseAndBajaFalse(String nombre);
}
