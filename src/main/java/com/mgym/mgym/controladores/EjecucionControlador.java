package com.mgym.mgym.controladores;

import java.time.Period;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mgym.mgym.MyException;
import com.mgym.mgym.enumeraciones.EjecucionTipo;
import com.mgym.mgym.servicios.EjecucionServicio;

@RestController
@RequestMapping("/api/ejecuciones")
public class EjecucionControlador {

    @Autowired
    private EjecucionServicio ejecucionServicio;

    /**
     * POST /api/ejecuciones
     * Crea la planificación de un ejercicio dentro de un día.
     * Define qué se va a hacer (ejercicio, tipo, reps objetivo).
     */
    @PostMapping
    public ResponseEntity<?> crearEjecucion(
            @RequestParam UUID ejercicioId,
            @RequestParam UUID diaId,
            @RequestParam EjecucionTipo tipo,
            @RequestParam int dificultad,
            @RequestParam int repeticionesObjetivo) {
        try {
            ejecucionServicio.crearEjecucion(ejercicioId, diaId, tipo, dificultad, repeticionesObjetivo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ejecucion creada correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * PATCH /api/ejecuciones/{id}/ejecutar
     * Registra los resultados reales al completar el ejercicio:
     * peso utilizado, repeticiones logradas, dificultad percibida y tiempo.
     * Este es el endpoint central del seguimiento de progresión.
     * 
     * El campo 'tiempo' acepta formato ISO-8601 de Period: ej. "PT1M30S" (1 min 30 seg).
     */
    @PatchMapping("/{id}/ejecutar")
    public ResponseEntity<?> ejecutar(
            @PathVariable UUID id,
            @RequestParam float peso,
            @RequestParam int dificultad,
            @RequestParam int repeticionesLogradas,
            @RequestParam(required = false) String tiempo) {
        Period period = (tiempo != null && !tiempo.isBlank()) ? Period.parse(tiempo) : null;
        ejecucionServicio.ejecutar(id, peso, dificultad, repeticionesLogradas, period);
        return ResponseEntity.ok("Ejecucion registrada correctamente");
    }
}
