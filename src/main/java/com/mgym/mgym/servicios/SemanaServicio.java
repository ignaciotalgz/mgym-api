package com.mgym.mgym.servicios;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgym.mgym.entidades.Rutina;
import com.mgym.mgym.entidades.Semana;
import com.mgym.mgym.excepciones.MyException;
import com.mgym.mgym.repositorios.RutinaRepositorio;
import com.mgym.mgym.repositorios.SemanaRepositorio;

@Service
public class SemanaServicio {

    @Autowired
    private SemanaRepositorio semanaRepositorio;
 
    @Autowired  // ← CORRECCIÓN: faltaba @Autowired en el servicio original
    private RutinaRepositorio rutinaRepositorio;
 
    // ── Escritura ──────────────────────────────────────────────────────────────
 
    /**
     * Crea una nueva semana dentro de una rutina.
     * Valida que la rutina exista, no esté dada de baja,
     * y que no haya otra semana con el mismo nombre en esa rutina.
     */
    @Transactional
    public void crearSemana(String nombre, UUID rutinaId) throws MyException {
        validarNombre(nombre);
        Rutina rutina = obtenerRutina(rutinaId);
        if (rutina.isBaja()) {
            throw new MyException("No se puede agregar una semana a una rutina dada de baja");
        }
        if (semanaRepositorio.existsByRutinaAndNombreIgnoreCase(rutina, nombre)) {
            throw new MyException("Ya existe una semana con el nombre '" + nombre + "' en esta rutina");
        }
        Semana nueva = new Semana();
        nueva.setNombre(nombre);
        nueva.setRutina(rutina);
        semanaRepositorio.save(nueva);
    }
 
    /**
     * Renombra una semana existente.
     * Valida que el nuevo nombre no esté en uso en la misma rutina.
     */
    @Transactional
    public void modificarSemana(UUID semanaId, String nuevoNombre) throws MyException {
        validarNombre(nuevoNombre);
        Semana semana = obtenerPorId(semanaId);
        if (!semana.getNombre().equalsIgnoreCase(nuevoNombre)
                && semanaRepositorio.existsByRutinaAndNombreIgnoreCase(semana.getRutina(), nuevoNombre)) {
            throw new MyException("Ya existe una semana con el nombre '" + nuevoNombre + "' en esta rutina");
        }
        semana.setNombre(nuevoNombre);
        semanaRepositorio.save(semana);
    }
 
    /**
     * Elimina una semana de la base de datos.
     * ATENCIÓN: solo usar si la semana no tiene días con ejecuciones registradas.
     * Para semanas con historial se recomienda no borrarlas físicamente.
     */
    @Transactional
    public void eliminarSemana(UUID semanaId) throws MyException {
        Semana semana = obtenerPorId(semanaId);
        semanaRepositorio.delete(semana);
    }
 
    // ── Lectura ────────────────────────────────────────────────────────────────
 
    /** Lista todas las semanas de una rutina ordenadas por nombre. */
    @Transactional(readOnly = true)
    public List<Semana> listarPorRutina(UUID rutinaId) throws MyException {
        Rutina rutina = obtenerRutina(rutinaId);
        return semanaRepositorio.findByRutinaOrderByNombreAsc(rutina);
    }
 
    /**
     * Obtiene una semana por su ID.
     * Lanza excepción si no existe.
     */
    @Transactional(readOnly = true)
    public Semana obtenerPorId(UUID semanaId) throws MyException {
        return semanaRepositorio.findById(semanaId)
                .orElseThrow(() -> new MyException("No se encontró la semana con id: " + semanaId));
    }
 
    /**
     * Obtiene una semana por nombre exacto dentro de una rutina.
     * Lanza excepción si no existe.
     */
    @Transactional(readOnly = true)
    public Semana obtenerPorNombre(UUID rutinaId, String nombre) throws MyException {
        Rutina rutina = obtenerRutina(rutinaId);
        return semanaRepositorio.findByRutinaAndNombre(rutina, nombre)
                .orElseThrow(() -> new MyException("No se encontró la semana '" + nombre + "' en esta rutina"));
    }
 
    /**
     * Cantidad de semanas que tiene una rutina.
     * Útil para nombrar automáticamente: "Semana " + (count + 1).
     */
    @Transactional(readOnly = true)
    public int contarPorRutina(UUID rutinaId) throws MyException {
        Rutina rutina = obtenerRutina(rutinaId);
        return semanaRepositorio.countByRutina(rutina);
    }
 
    // ── Validaciones privadas ──────────────────────────────────────────────────
 
    private void validarNombre(String nombre) throws MyException {
        if (nombre == null || nombre.isBlank()) {
            throw new MyException("El nombre no puede ser nulo ni vacío");
        }
    }
 
    private Rutina obtenerRutina(UUID rutinaId) throws MyException {
        return rutinaRepositorio.findById(rutinaId)
                .orElseThrow(() -> new MyException("No se encontró la rutina con id: " + rutinaId));
    }
}
