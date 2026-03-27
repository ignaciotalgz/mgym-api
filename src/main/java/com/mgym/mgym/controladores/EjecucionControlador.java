package com.mgym.mgym.controladores;

import java.time.Period;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mgym.mgym.enumeraciones.EjecucionTipo;
import com.mgym.mgym.excepciones.MyException;
import com.mgym.mgym.modelos.EjecucionCreateDTO;
import com.mgym.mgym.servicios.EjecucionServicio;

@RestController
@RequestMapping("/api/ejecuciones")
public class EjecucionControlador {

    @Autowired
    private EjecucionServicio ejecucionServicio;
 
    // ── Escritura ──────────────────────────────────────────────────────────────
 
    /**
     * POST /api/ejecuciones
     * PLANIFICACIÓN: define qué ejercicio se hará en un día, con qué técnica
     * y cuántas repeticiones se apuntan. Sin peso ni resultado todavía.
     */
    @PostMapping
    public ResponseEntity<?> crearEjecucion(@RequestBody EjecucionCreateDTO ejecucionCreateDTO) {
        try {
            ejecucionServicio.crearEjecucion(ejecucionCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ejecucion planificada correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * PATCH /api/ejecuciones/{id}/ejecutar
     * EJECUCIÓN: registra los resultados reales al completar el ejercicio.
     * Es el endpoint central del seguimiento de progresión.
     * El campo 'tiempo' es opcional y acepta formato ISO-8601 Period: ej. "PT1M30S" (1 min 30 seg).
     */
    @PatchMapping("/{id}/ejecutar")
    public ResponseEntity<?> ejecutar(
            @PathVariable UUID id,
            @RequestParam float peso,
            @RequestParam int repeticionesLogradas,
            @RequestParam(required = false) String tiempo) {
        try {
            Period period = (tiempo != null && !tiempo.isBlank()) ? Period.parse(tiempo) : null;
            ejecucionServicio.ejecutar(id, peso, repeticionesLogradas, period);
            return ResponseEntity.ok("Ejecucion registrada correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * DELETE /api/ejecuciones/{id}
     * Elimina una ejecución planificada que aún no fue realizada.
     * No permite eliminar ejecuciones ya completadas (protege el historial).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEjecucion(@PathVariable UUID id) {
        try {
            ejecucionServicio.eliminarEjecucion(id);
            return ResponseEntity.ok("Ejecucion eliminada correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    // ── Lectura: por día ───────────────────────────────────────────────────────
 
    /**
     * GET /api/ejecuciones?diaId={id}
     * Todas las ejecuciones de un día (pendientes y completadas), ordenadas por ejercicio.
     * Vista principal de la sesión de entrenamiento.
     */
    @GetMapping
    public ResponseEntity<?> listarPorDia(@RequestParam UUID diaId) {
        try {
            return ResponseEntity.ok(ejecucionServicio.listarPorDia(diaId));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * GET /api/ejecuciones/pendientes?diaId={id}
     * Solo las ejecuciones pendientes de un día (sin repeticiones logradas).
     * Muestra "qué falta por hacer" en la sesión actual.
     */
    @GetMapping("/pendientes")
    public ResponseEntity<?> listarPendientesPorDia(@RequestParam UUID diaId) {
        try {
            return ResponseEntity.ok(ejecucionServicio.listarPendientesPorDia(diaId));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * GET /api/ejecuciones/por-tipo?diaId={id}&tipo=Normal
     * Ejecuciones de un día filtradas por tipo de técnica.
     */
    @GetMapping("/por-tipo")
    public ResponseEntity<?> listarPorDiaYTipo(
            @RequestParam UUID diaId,
            @RequestParam EjecucionTipo tipo) {
        try {
            return ResponseEntity.ok(ejecucionServicio.listarPorDiaYTipo(diaId, tipo));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * GET /api/ejecuciones/contar?diaId={id}
     * Cantidad de ejercicios planificados para un día.
     */
    @GetMapping("/contar")
    public ResponseEntity<?> contarPorDia(@RequestParam UUID diaId) {
        try {
            return ResponseEntity.ok(ejecucionServicio.contarPorDia(diaId));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    // ── Lectura: progresión por ejercicio ──────────────────────────────────────
 
    /**
     * GET /api/ejecuciones/historial?ejercicioId={id}
     * Historial completo de un ejercicio en orden cronológico (más viejo primero).
     * Base para graficar la curva de progresión.
     */
    @GetMapping("/historial")
    public ResponseEntity<?> listarHistorial(@RequestParam UUID ejercicioId) {
        try {
            return ResponseEntity.ok(ejecucionServicio.listarHistorialPorEjercicio(ejercicioId));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * GET /api/ejecuciones/historial/reciente?ejercicioId={id}
     * Historial de un ejercicio, más reciente primero.
     * Para mostrar "las últimas veces que hiciste este ejercicio".
     */
    @GetMapping("/historial/reciente")
    public ResponseEntity<?> listarHistorialReciente(@RequestParam UUID ejercicioId) {
        try {
            return ResponseEntity.ok(ejecucionServicio.listarHistorialRecientePorEjercicio(ejercicioId));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * GET /api/ejecuciones/completadas?ejercicioId={id}
     * Solo las ejecuciones con repeticiones logradas > 0 para un ejercicio.
     * Filtra las planificadas que nunca se realizaron.
     */
    @GetMapping("/completadas")
    public ResponseEntity<?> listarCompletadas(@RequestParam UUID ejercicioId) {
        try {
            return ResponseEntity.ok(ejecucionServicio.listarCompletadasPorEjercicio(ejercicioId));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * GET /api/ejecuciones/por-tipo-ejercicio?ejercicioId={id}&tipo=DropSet
     * Historial de un ejercicio filtrado por tipo de técnica.
     * Permite comparar la progresión específica de, por ejemplo, solo DropSets.
     */
    @GetMapping("/por-tipo-ejercicio")
    public ResponseEntity<?> listarPorEjercicioYTipo(
            @RequestParam UUID ejercicioId,
            @RequestParam EjecucionTipo tipo) {
        try {
            return ResponseEntity.ok(ejecucionServicio.listarPorEjercicioYTipo(ejercicioId, tipo));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * GET /api/ejecuciones/peso-minimo?ejercicioId={id}&pesoMinimo=60
     * Ejecuciones de un ejercicio que superan un peso mínimo.
     * Útil para filtrar solo series de trabajo, descartando calentamientos.
     */
    @GetMapping("/peso-minimo")
    public ResponseEntity<?> listarPorPesoMinimo(
            @RequestParam UUID ejercicioId,
            @RequestParam float pesoMinimo) {
        try {
            return ResponseEntity.ok(ejecucionServicio.listarPorEjercicioConPesoMinimo(ejercicioId, pesoMinimo));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * GET /api/ejecuciones/record?ejercicioId={id}
     * Récord personal: la ejecución de mayor peso registrada para un ejercicio.
     * Devuelve 204 No Content si el ejercicio nunca fue ejecutado con peso.
     */
    @GetMapping("/record")
    public ResponseEntity<?> obtenerRecord(@RequestParam UUID ejercicioId) {
        try {
            var record = ejecucionServicio.obtenerRecordPeso(ejercicioId);
            if (record == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(record);
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * GET /api/ejecuciones/frecuencia?ejercicioId={id}
     * Cuántas veces se ha ejecutado un ejercicio en total.
     * Indicador de frecuencia de entrenamiento.
     */
    @GetMapping("/frecuencia")
    public ResponseEntity<?> contarEjecucionesPorEjercicio(@RequestParam UUID ejercicioId) {
        try {
            return ResponseEntity.ok(ejecucionServicio.contarEjecucionesPorEjercicio(ejercicioId));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
 
    /**
     * GET /api/ejecuciones/{id}
     * Obtiene una ejecución por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(ejecucionServicio.obtenerPorId(id));
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
