package com.mgym.mgym.repositorios;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mgym.mgym.entidades.Rutina;
import com.mgym.mgym.entidades.Usuario;

@Repository
public interface RutinaRepositorio extends JpaRepository<Rutina, UUID>{
    // ── Por usuario ────────────────────────────────────────────────────────────
 
    /** Todas las rutinas de un usuario (activas y dadas de baja). */
    List<Rutina> findByUsuario(Usuario usuario);
 
    /** Solo las rutinas activas (no dadas de baja) de un usuario. */
    List<Rutina> findByUsuarioAndBajaFalse(Usuario usuario);
 
    /** Solo las rutinas dadas de baja de un usuario (auditoría / historial). */
    List<Rutina> findByUsuarioAndBajaTrue(Usuario usuario);
 
    // ── Rutina actual ──────────────────────────────────────────────────────────
 
    /**
     * La rutina marcada como actual para un usuario.
     * Debe haber como máximo una por usuario — el servicio es responsable
     * de desmarcar las demás antes de marcar una nueva.
     */
    Optional<Rutina> findByUsuarioAndActualTrue(Usuario usuario);
 
    // ── Búsqueda por nombre ────────────────────────────────────────────────────
 
    /** Rutinas activas de un usuario que contengan el texto en su nombre. */
    List<Rutina> findByUsuarioAndNombreContainingIgnoreCaseAndBajaFalse(Usuario usuario, String texto);
 
    // ── Validaciones ───────────────────────────────────────────────────────────
 
    /** Verificar si un usuario ya tiene una rutina activa con ese nombre exacto. */
    boolean existsByUsuarioAndNombreIgnoreCaseAndBajaFalse(Usuario usuario, String nombre);
}
