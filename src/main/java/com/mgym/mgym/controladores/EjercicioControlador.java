package com.mgym.mgym.controladores;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mgym.mgym.entidades.Ejercicio;
import com.mgym.mgym.excepciones.MyException;
import com.mgym.mgym.modelos.EjercicioCreateDTO;
import com.mgym.mgym.servicios.EjercicioServicio;

@RestController
@RequestMapping("/api/ejercicios")
public class EjercicioControlador {

    @Autowired
    private EjercicioServicio ejercicioServicio;
 
    // ── Escritura ──────────────────────────────────────────────────────────────
 
    /**
     * POST /api/ejercicios
     * Crea un nuevo ejercicio. Valida nombre no vacío y sin duplicados.
     */
    @PostMapping
    public ResponseEntity<?> crearEjercicio(@RequestBody EjercicioCreateDTO ejercicioCreateDTO) {
        try {
            ejercicioServicio.crearEjercicio(ejercicioCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ejercicio creado correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * PUT /api/ejercicios/{id}
     * Modifica nombre y/o descripción de un ejercicio existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> modificarEjercicio(
            @PathVariable UUID id,
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion) {
        try {
            ejercicioServicio.modificarEjercicio(id, nombre, descripcion);
            return ResponseEntity.ok("Ejercicio modificado correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * DELETE /api/ejercicios/{id}
     * Baja lógica: el ejercicio se oculta pero se conserva el historial.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> bajaEjercicio(@PathVariable UUID id) {
        try {
            ejercicioServicio.bajaEjercicio(id);
            return ResponseEntity.ok("Ejercicio dado de baja correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    // ── Lectura ────────────────────────────────────────────────────────────────
 
    /**
     * GET /api/ejercicios
     * Lista todos los ejercicios activos (no dados de baja). Uso principal.
     */
    @GetMapping
    public ResponseEntity<List<Ejercicio>> listarActivos() {
        return ResponseEntity.ok(ejercicioServicio.listarActivos());
    }
 
    /**
     * GET /api/ejercicios/todos
     * Lista todos los ejercicios incluyendo dados de baja. Para auditoría.
     */
    @GetMapping("/todos")
    public ResponseEntity<List<Ejercicio>> listarTodos() {
        return ResponseEntity.ok(ejercicioServicio.listarTodos());
    }
 
    /**
     * GET /api/ejercicios/baja
     * Lista solo los ejercicios dados de baja. Para historial/auditoría.
     */
    @GetMapping("/baja")
    public ResponseEntity<List<Ejercicio>> listarDadosDeBaja() {
        return ResponseEntity.ok(ejercicioServicio.listarDadosDeBaja());
    }
 
    /**
     * GET /api/ejercicios/{id}
     * Obtiene un ejercicio por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(ejercicioServicio.obtenerPorId(id));
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
 
    /**
     * GET /api/ejercicios/buscar?texto=press
     * Búsqueda parcial por nombre. Útil para autocomplete en el frontend.
     */
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPorNombre(@RequestParam String texto) {
        try {
            return ResponseEntity.ok(ejercicioServicio.buscarPorNombre(texto));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * GET /api/ejercicios/nombre?nombre=Press banca
     * Obtiene un ejercicio activo por nombre exacto.
     */
    @GetMapping("/nombre")
    public ResponseEntity<?> obtenerPorNombre(@RequestParam String nombre) {
        try {
            return ResponseEntity.ok(ejercicioServicio.obtenerPorNombre(nombre));
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
