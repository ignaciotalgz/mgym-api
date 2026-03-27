package com.mgym.mgym.controladores;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mgym.mgym.excepciones.MyException;
import com.mgym.mgym.servicios.DiaServicio;

@RestController
@RequestMapping("/api/dias")
public class DiaControlador {

    @Autowired
    private DiaServicio diaServicio;
 
    // ── Escritura ──────────────────────────────────────────────────────────────
 
    /**
     * POST /api/dias
     * Crea un nuevo día dentro de una semana, sin fecha aún.
     * La fecha se asigna después con /registrar-fecha cuando se entrena.
     */
    @PostMapping
    public ResponseEntity<?> crearDia(
            @RequestParam String nombre,
            @RequestParam UUID semanaId) {
        try {
            diaServicio.crearDia(nombre, semanaId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Dia creado correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * PUT /api/dias/{id}
     * Renombra un día existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> modificarDia(
            @PathVariable UUID id,
            @RequestParam String nombre) {
        try {
            diaServicio.modificarDia(id, nombre);
            return ResponseEntity.ok("Dia modificado correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * PATCH /api/dias/{id}/registrar-fecha
     * Registra la fecha real en que se entrenó este día.
     * Se llama al iniciar la sesión de entrenamiento.
     * Formato de fecha esperado: yyyy-MM-dd (ej: 2025-03-24)
     */
    @PatchMapping("/{id}/registrar-fecha")
    public ResponseEntity<?> registrarFecha(
            @PathVariable UUID id,
            @RequestParam String fecha) {
        try {
            Date fechaParseada = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
            diaServicio.registrarFecha(id, fechaParseada);
            return ResponseEntity.ok("Fecha registrada correctamente");
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Formato de fecha inválido. Use yyyy-MM-dd");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * DELETE /api/dias/{id}
     * Eliminación física. Solo usar si el día no tiene ejecuciones realizadas.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDia(@PathVariable UUID id) {
        try {
            diaServicio.eliminarDia(id);
            return ResponseEntity.ok("Dia eliminado correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    // ── Lectura ────────────────────────────────────────────────────────────────
 
    /**
     * GET /api/dias?semanaId={id}
     * Lista todos los días de una semana ordenados por nombre.
     */
    @GetMapping
    public ResponseEntity<?> listarPorSemana(@RequestParam UUID semanaId) {
        try {
            return ResponseEntity.ok(diaServicio.listarPorSemana(semanaId));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * GET /api/dias/por-fecha?semanaId={id}
     * Lista los días de una semana ordenados por fecha real de entrenamiento.
     */
    @GetMapping("/por-fecha")
    public ResponseEntity<?> listarPorSemanaOrdenadoPorFecha(@RequestParam UUID semanaId) {
        try {
            return ResponseEntity.ok(diaServicio.listarPorSemanaOrdenadoPorFecha(semanaId));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * GET /api/dias/historial?desde=yyyy-MM-dd&hasta=yyyy-MM-dd
     * Lista los días entrenados en un rango de fechas.
     * Base para reportes: "qué entrené en las últimas 4 semanas".
     */
    @GetMapping("/historial")
    public ResponseEntity<?> listarPorRangoDeFechas(
            @RequestParam String desde,
            @RequestParam String hasta) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaDesde = sdf.parse(desde);
            Date fechaHasta = sdf.parse(hasta);
            return ResponseEntity.ok(diaServicio.listarPorRangoDeFechas(fechaDesde, fechaHasta));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Formato de fecha inválido. Use yyyy-MM-dd");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * GET /api/dias/fecha?fecha=yyyy-MM-dd
     * Obtiene el día que corresponde a una fecha real específica.
     */
    @GetMapping("/fecha")
    public ResponseEntity<?> obtenerPorFecha(@RequestParam String fecha) {
        try {
            Date fechaParseada = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
            return ResponseEntity.ok(diaServicio.obtenerPorFecha(fechaParseada));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body("Formato de fecha inválido. Use yyyy-MM-dd");
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
 
    /**
     * GET /api/dias/{id}
     * Obtiene un día por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(diaServicio.obtenerPorId(id));
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
 
    /**
     * GET /api/dias/contar?semanaId={id}
     * Devuelve la cantidad de días de una semana.
     * Útil para sugerir el nombre: "Día " + (count + 1).
     */
    @GetMapping("/contar")
    public ResponseEntity<?> contarPorSemana(@RequestParam UUID semanaId) {
        try {
            return ResponseEntity.ok(diaServicio.contarPorSemana(semanaId));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
