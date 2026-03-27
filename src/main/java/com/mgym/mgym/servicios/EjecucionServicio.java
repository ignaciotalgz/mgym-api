package com.mgym.mgym.servicios;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgym.mgym.entidades.Dia;
import com.mgym.mgym.entidades.Ejecucion;
import com.mgym.mgym.entidades.Ejercicio;
import com.mgym.mgym.enumeraciones.EjecucionTipo;
import com.mgym.mgym.excepciones.MyException;
import com.mgym.mgym.modelos.EjecucionCreateDTO;
import com.mgym.mgym.repositorios.DiaRepositorio;
import com.mgym.mgym.repositorios.EjecucionRepositorio;
import com.mgym.mgym.repositorios.EjercicioRepositorio;

@Service
public class EjecucionServicio {
     @Autowired
    private EjecucionRepositorio ejecucionRepositorio;
 
    @Autowired  // ← CORRECCIÓN: faltaba @Autowired en el servicio original
    private EjercicioRepositorio ejercicioRepositorio;
 
    @Autowired  // ← CORRECCIÓN: faltaba @Autowired en el servicio original
    private DiaRepositorio diaRepositorio;
 
    // ── Escritura ──────────────────────────────────────────────────────────────
 
    /**
     * Planifica un ejercicio dentro de un día: define qué se va a hacer,
     * con qué tipo de técnica y cuántas repeticiones se apuntan.
     * El peso, tiempo y repeticionesLogradas quedan en 0 hasta ejecutar.
     *
     * Este es el paso de PLANIFICACIÓN — se llama al armar la sesión.
     */
    @Transactional
    public void crearEjecucion(EjecucionCreateDTO ejecucionCreateDTO) throws MyException {
 
        Ejercicio ejercicio = obtenerEjercicio(ejecucionCreateDTO.getEjercicioId());
        Dia dia = obtenerDia(ejecucionCreateDTO.getDiaId());
 
        if (ejercicio.isBaja()) {
            throw new MyException("No se puede planificar un ejercicio dado de baja");
        }
        if (ejecucionCreateDTO.getRepeticionesObjetivo() <= 0) {
            throw new MyException("Las repeticiones objetivo deben ser mayor a cero");
        }
 
        Ejecucion ejecucion = new Ejecucion();
        ejecucion.setEjercicio(ejercicio);
        ejecucion.setDia(dia);
        ejecucion.setTipo(ejecucionCreateDTO.getTipo());
        ejecucion.setDificultad(ejecucionCreateDTO.getDificultad());
        ejecucion.setRepeticionesObjetivo(ejecucionCreateDTO.getRepeticionesObjetivo());
        // peso, tiempo y repeticionesLogradas quedan en 0 hasta que se ejecute
        ejecucionRepositorio.save(ejecucion);
    }
 
    /**
     * Registra los resultados reales al completar un ejercicio.
     * Este es el paso de EJECUCIÓN — se llama durante o después del entrenamiento.
     *
     * @param peso              Peso utilizado en kg (puede ser 0 para ejercicios sin peso)
     * @param repeticionesLogradas Cuántas repeticiones se lograron efectivamente
     * @param tiempo            Duración del ejercicio en formato Period ISO-8601 (puede ser null)
     */
    @Transactional
    public void ejecutar(UUID ejecucionId, float peso, int repeticionesLogradas, Period tiempo) throws MyException {
 
        if (peso < 0) {
            throw new MyException("El peso no puede ser negativo");
        }
        if (repeticionesLogradas < 0) {
            throw new MyException("Las repeticiones logradas no pueden ser negativas");
        }
 
        Ejecucion ejecucion = obtenerPorId(ejecucionId);
        ejecucion.setPeso(peso);
        ejecucion.setRepeticionesLogradas(repeticionesLogradas);
        ejecucion.setTiempo(tiempo);
        ejecucionRepositorio.save(ejecucion);
    }
 
    /**
     * Elimina una ejecución planificada que todavía no fue realizada.
     * No permite eliminar ejecuciones ya completadas para preservar el historial.
     */
    @Transactional
    public void eliminarEjecucion(UUID ejecucionId) throws MyException {
        Ejecucion ejecucion = obtenerPorId(ejecucionId);
        if (ejecucion.getRepeticionesLogradas() > 0) {
            throw new MyException("No se puede eliminar una ejecución ya realizada. El historial debe conservarse.");
        }
        ejecucionRepositorio.delete(ejecucion);
    }
 
    // ── Lectura: por día ───────────────────────────────────────────────────────
 
    /**
     * Obtiene todas las ejecuciones planificadas para un día.
     * Incluye las pendientes y las ya completadas.
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> listarPorDia(UUID diaId) throws MyException {
        Dia dia = obtenerDia(diaId);
        return ejecucionRepositorio.findByDiaOrderByEjercicioNombreAsc(dia);
    }
 
    /**
     * Obtiene solo las ejecuciones pendientes de un día
     * (planificadas pero sin repeticiones logradas aún).
     * Útil para saber qué queda por hacer en la sesión.
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> listarPendientesPorDia(UUID diaId) throws MyException {
        Dia dia = obtenerDia(diaId);
        return ejecucionRepositorio.findByDiaAndRepeticionesLogradas(dia, 0);
    }
 
    /**
     * Filtra ejecuciones de un día por tipo de técnica (Normal, RestPause, DropSet).
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> listarPorDiaYTipo(UUID diaId, EjecucionTipo tipo) throws MyException {
        Dia dia = obtenerDia(diaId);
        return ejecucionRepositorio.findByDiaAndTipo(dia, tipo);
    }
 
    /**
     * Cantidad de ejercicios planificados para un día.
     */
    @Transactional(readOnly = true)
    public int contarPorDia(UUID diaId) throws MyException {
        Dia dia = obtenerDia(diaId);
        return ejecucionRepositorio.countByDia(dia);
    }
 
    // ── Lectura: progresión por ejercicio ──────────────────────────────────────
 
    /**
     * Historial completo de un ejercicio ordenado cronológicamente (más viejo primero).
     * Esta es la base de la curva de progresión.
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> listarHistorialPorEjercicio(UUID ejercicioId) throws MyException {
        Ejercicio ejercicio = obtenerEjercicio(ejercicioId);
        return ejecucionRepositorio.findByEjercicioOrderByDiaDiaAsc(ejercicio);
    }
 
    /**
     * Historial de un ejercicio, más reciente primero.
     * Conveniente para mostrar "las últimas veces que hiciste este ejercicio".
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> listarHistorialRecientePorEjercicio(UUID ejercicioId) throws MyException {
        Ejercicio ejercicio = obtenerEjercicio(ejercicioId);
        return ejecucionRepositorio.findByEjercicioOrderByDiaDiaDesc(ejercicio);
    }
 
    /**
     * Solo las ejecuciones completadas de un ejercicio (con reps logradas > 0).
     * Filtra las planificadas que nunca se realizaron.
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> listarCompletadasPorEjercicio(UUID ejercicioId) throws MyException {
        Ejercicio ejercicio = obtenerEjercicio(ejercicioId);
        return ejecucionRepositorio.findByEjercicioAndRepeticionesLogradasGreaterThan(ejercicio, 0);
    }
 
    /**
     * Historial de un ejercicio filtrado por tipo de técnica.
     * Permite comparar progresión específica de, por ejemplo, solo DropSets.
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> listarPorEjercicioYTipo(UUID ejercicioId, EjecucionTipo tipo) throws MyException {
        Ejercicio ejercicio = obtenerEjercicio(ejercicioId);
        return ejecucionRepositorio.findByEjercicioAndTipo(ejercicio, tipo);
    }
 
    /**
     * Todas las ejecuciones de un ejercicio que superen un peso mínimo.
     * Útil para filtrar solo las series de trabajo descartando calentamientos.
     */
    @Transactional(readOnly = true)
    public List<Ejecucion> listarPorEjercicioConPesoMinimo(UUID ejercicioId, float pesoMinimo) throws MyException {
        if (pesoMinimo < 0) {
            throw new MyException("El peso mínimo no puede ser negativo");
        }
        Ejercicio ejercicio = obtenerEjercicio(ejercicioId);
        return ejecucionRepositorio.findByEjercicioAndPesoGreaterThanEqual(ejercicio, pesoMinimo);
    }
 
    /**
     * Récord personal: la ejecución de mayor peso registrada para un ejercicio.
     * Retorna null si el ejercicio nunca fue ejecutado con peso.
     */
    @Transactional(readOnly = true)
    public Ejecucion obtenerRecordPeso(UUID ejercicioId) throws MyException {
        Ejercicio ejercicio = obtenerEjercicio(ejercicioId);
        Optional<Ejecucion> record = ejecucionRepositorio.findMaxPesoByEjercicio(ejercicio);
        return record.orElse(null);
    }
 
    /**
     * Cuántas veces se ha ejecutado un ejercicio en total.
     * Indicador de frecuencia de entrenamiento.
     */
    @Transactional(readOnly = true)
    public int contarEjecucionesPorEjercicio(UUID ejercicioId) throws MyException {
        Ejercicio ejercicio = obtenerEjercicio(ejercicioId);
        return ejecucionRepositorio.countByEjercicio(ejercicio);
    }
 
    /**
     * Obtiene una ejecución por su ID.
     * Lanza excepción si no existe.
     */
    @Transactional(readOnly = true)
    public Ejecucion obtenerPorId(UUID ejecucionId) throws MyException {
        return ejecucionRepositorio.findById(ejecucionId)
                .orElseThrow(() -> new MyException("No se encontró la ejecución con id: " + ejecucionId));
    }
 
    // ── Helpers privados ───────────────────────────────────────────────────────
 
    private Ejercicio obtenerEjercicio(UUID ejercicioId) throws MyException {
        return ejercicioRepositorio.findById(ejercicioId)
                .orElseThrow(() -> new MyException("No se encontró el ejercicio con id: " + ejercicioId));
    }
 
    private Dia obtenerDia(UUID diaId) throws MyException {
        return diaRepositorio.findById(diaId)
                .orElseThrow(() -> new MyException("No se encontró el día con id: " + diaId));
    }
}
