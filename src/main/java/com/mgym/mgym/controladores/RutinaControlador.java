package com.mgym.mgym.controladores;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mgym.mgym.entidades.Rutina;
import com.mgym.mgym.excepciones.MyException;
import com.mgym.mgym.modelos.RutinaCreateDTO;
import com.mgym.mgym.servicios.RutinaServicio;

@RestController
@RequestMapping("/api/rutinas")
public class RutinaControlador {

    @Autowired
    private RutinaServicio rutinaServicio;
 
    // ── Escritura ──────────────────────────────────────────────────────────────
 
    /**
     * POST /api/rutinas
     * Crea una nueva rutina para el usuario. Nace inactiva (actual = false).
     */
    @PostMapping
    public ResponseEntity<?> crearRutina(
            @RequestBody RutinaCreateDTO rutinaCreateDTO) {
        try {
            rutinaServicio.crearRutina(rutinaCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Rutina creada correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * DELETE /api/rutinas/{id}
     * Baja lógica. Si era la rutina actual, también la desactiva.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> bajaRutina(@PathVariable UUID id) {
        try {
            rutinaServicio.bajaRutina(id);
            return ResponseEntity.ok("Rutina dada de baja correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * PATCH /api/rutinas/{id}/actual
     * Define esta rutina como la actual del usuario.
     * Desactiva automáticamente cualquier otra rutina activa previa.
     */
    @PatchMapping("/{id}/actual")
    public ResponseEntity<?> definirActual(
            @PathVariable UUID id,
            @RequestParam UUID usuarioId) {
        try {
            rutinaServicio.definirActual(id, usuarioId);
            return ResponseEntity.ok("Rutina actual definida correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    // ── Lectura ────────────────────────────────────────────────────────────────
 
    /**
     * GET /api/rutinas?usuarioId={id}
     * Lista todas las rutinas activas (no dadas de baja) de un usuario.
     */
    @GetMapping
    public ResponseEntity<?> listarActivas(@RequestParam UUID usuarioId) {
        try {
            List<Rutina> rutinas = rutinaServicio.listarActivasPorUsuario(usuarioId);
            return ResponseEntity.ok(rutinas);
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * GET /api/rutinas/todas?usuarioId={id}
     * Lista todas las rutinas del usuario incluyendo las dadas de baja.
     */
    @GetMapping("/todas")
    public ResponseEntity<?> listarTodas(@RequestParam UUID usuarioId) {
        try {
            return ResponseEntity.ok(rutinaServicio.listarTodasPorUsuario(usuarioId));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * GET /api/rutinas/baja?usuarioId={id}
     * Lista las rutinas dadas de baja del usuario. Para auditoría/historial.
     */
    @GetMapping("/baja")
    public ResponseEntity<?> listarDadasDeBaja(@RequestParam UUID usuarioId) {
        try {
            return ResponseEntity.ok(rutinaServicio.listarDadasDeBajaPorUsuario(usuarioId));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * GET /api/rutinas/actual?usuarioId={id}
     * Obtiene la rutina marcada como actual para el usuario.
     */
    @GetMapping("/actual")
    public ResponseEntity<?> obtenerActual(@RequestParam UUID usuarioId) {
        try {
            return ResponseEntity.ok(rutinaServicio.obtenerActual(usuarioId));
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
 
    /**
     * GET /api/rutinas/{id}
     * Obtiene una rutina por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(rutinaServicio.obtenerPorId(id));
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
 
    /**
     * GET /api/rutinas/buscar?usuarioId={id}&texto=fuerza
     * Búsqueda de rutinas activas por nombre parcial dentro del usuario.
     */
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorNombre(
            @RequestParam UUID usuarioId,
            @RequestParam String texto) {
        try {
            return ResponseEntity.ok(rutinaServicio.buscarPorNombre(usuarioId, texto));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
