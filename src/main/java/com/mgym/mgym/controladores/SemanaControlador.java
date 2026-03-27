package com.mgym.mgym.controladores;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mgym.mgym.excepciones.MyException;
import com.mgym.mgym.servicios.SemanaServicio;

@RestController
@RequestMapping("/api/semanas")
public class SemanaControlador {

    @Autowired
    private SemanaServicio semanaServicio;
 
    // ── Escritura ──────────────────────────────────────────────────────────────
 
    /**
     * POST /api/semanas
     * Crea una nueva semana dentro de una rutina.
     * Valida que la rutina no esté dada de baja y que el nombre no esté repetido.
     */
    @PostMapping
    public ResponseEntity<?> crearSemana(
            @RequestParam String nombre,
            @RequestParam UUID rutinaId) {
        try {
            semanaServicio.crearSemana(nombre, rutinaId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Semana creada correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * PUT /api/semanas/{id}
     * Renombra una semana existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> modificarSemana(
            @PathVariable UUID id,
            @RequestParam String nombre) {
        try {
            semanaServicio.modificarSemana(id, nombre);
            return ResponseEntity.ok("Semana modificada correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * DELETE /api/semanas/{id}
     * Eliminación física. Solo usar si la semana no tiene días con ejecuciones realizadas.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarSemana(@PathVariable UUID id) {
        try {
            semanaServicio.eliminarSemana(id);
            return ResponseEntity.ok("Semana eliminada correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    // ── Lectura ────────────────────────────────────────────────────────────────
 
    /**
     * GET /api/semanas?rutinaId={id}
     * Lista todas las semanas de una rutina ordenadas por nombre.
     */
    @GetMapping
    public ResponseEntity<?> listarPorRutina(@RequestParam UUID rutinaId) {
        try {
            return ResponseEntity.ok(semanaServicio.listarPorRutina(rutinaId));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * GET /api/semanas/{id}
     * Obtiene una semana por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(semanaServicio.obtenerPorId(id));
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
 
    /**
     * GET /api/semanas/nombre?rutinaId={id}&nombre=Semana 1
     * Obtiene una semana por nombre exacto dentro de una rutina.
     */
    @GetMapping("/nombre")
    public ResponseEntity<?> obtenerPorNombre(
            @RequestParam UUID rutinaId,
            @RequestParam String nombre) {
        try {
            return ResponseEntity.ok(semanaServicio.obtenerPorNombre(rutinaId, nombre));
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
 
    /**
     * GET /api/semanas/contar?rutinaId={id}
     * Devuelve la cantidad de semanas de una rutina.
     * Útil para el frontend para sugerir el nombre: "Semana " + (count + 1).
     */
    @GetMapping("/contar")
    public ResponseEntity<?> contarPorRutina(@RequestParam UUID rutinaId) {
        try {
            return ResponseEntity.ok(semanaServicio.contarPorRutina(rutinaId));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
