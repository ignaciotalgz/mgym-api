package com.mgym.mgym.servicios;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgym.mgym.entidades.Dia;
import com.mgym.mgym.entidades.Semana;
import com.mgym.mgym.excepciones.MyException;
import com.mgym.mgym.repositorios.DiaRepositorio;
import com.mgym.mgym.repositorios.SemanaRepositorio;

@Service
public class DiaServicio {
    @Autowired
    private DiaRepositorio diaRepositorio;
 
    @Autowired  // ← CORRECCIÓN: faltaba @Autowired en el servicio original
    private SemanaRepositorio semanaRepositorio;
 
    // ── Escritura ──────────────────────────────────────────────────────────────
 
    /**
     * Crea un nuevo día dentro de una semana.
     * El día nace sin fecha (se asignará cuando se entrene ese día).
     * Valida que no exista ya un día con ese nombre en la misma semana.
     */
    @Transactional
    public void crearDia(String nombre, UUID semanaId) throws MyException {
        validarNombre(nombre);
        Semana semana = obtenerSemana(semanaId);
        if (diaRepositorio.existsBySemanaAndNombreIgnoreCase(semana, nombre)) {
            throw new MyException("Ya existe un día con el nombre '" + nombre + "' en esta semana");
        }
        Dia nuevo = new Dia();
        nuevo.setNombre(nombre);
        nuevo.setSemana(semana);
        diaRepositorio.save(nuevo);
    }
 
    /**
     * Renombra un día existente.
     */
    @Transactional
    public void modificarDia(UUID diaId, String nuevoNombre) throws MyException {
        validarNombre(nuevoNombre);
        Dia dia = obtenerPorId(diaId);
        if (!dia.getNombre().equalsIgnoreCase(nuevoNombre)
                && diaRepositorio.existsBySemanaAndNombreIgnoreCase(dia.getSemana(), nuevoNombre)) {
            throw new MyException("Ya existe un día con el nombre '" + nuevoNombre + "' en esta semana");
        }
        dia.setNombre(nuevoNombre);
        diaRepositorio.save(dia);
    }
 
    /**
     * Registra la fecha real en la que se entrenó este día.
     * Se llama al iniciar la sesión de entrenamiento.
     * Valida que no haya otro día ya registrado en esa misma fecha.
     */
    @Transactional
    public void registrarFecha(UUID diaId, Date fecha) throws MyException {
        if (fecha == null) {
            throw new MyException("La fecha no puede ser nula");
        }
        if (diaRepositorio.existsByDia(fecha)) {
            throw new MyException("Ya existe un día registrado con la fecha: " + fecha);
        }
        Dia dia = obtenerPorId(diaId);
        dia.setDia(fecha);
        diaRepositorio.save(dia);
    }
 
    /**
     * Elimina un día físicamente.
     * Solo recomendado si el día no tiene ejecuciones realizadas.
     */
    @Transactional
    public void eliminarDia(UUID diaId) throws MyException {
        Dia dia = obtenerPorId(diaId);
        diaRepositorio.delete(dia);
    }
 
    // ── Lectura ────────────────────────────────────────────────────────────────
 
    /** Lista todos los días de una semana ordenados por nombre. */
    @Transactional(readOnly = true)
    public List<Dia> listarPorSemana(UUID semanaId) throws MyException {
        Semana semana = obtenerSemana(semanaId);
        return diaRepositorio.findBySemanaOrderByNombreAsc(semana);
    }
 
    /**
     * Lista los días de una semana ordenados por fecha real.
     * Útil para ver el historial cronológico de una semana entrenada.
     */
    @Transactional(readOnly = true)
    public List<Dia> listarPorSemanaOrdenadoPorFecha(UUID semanaId) throws MyException {
        Semana semana = obtenerSemana(semanaId);
        return diaRepositorio.findBySemanaOrderByDiaAsc(semana);
    }
 
    /**
     * Lista los días entrenados en un rango de fechas.
     * Base para reportes de actividad: "qué entrené en las últimas 4 semanas".
     */
    @Transactional(readOnly = true)
    public List<Dia> listarPorRangoDeFechas(Date desde, Date hasta) throws MyException {
        if (desde == null || hasta == null) {
            throw new MyException("Las fechas de rango no pueden ser nulas");
        }
        if (desde.after(hasta)) {
            throw new MyException("La fecha 'desde' no puede ser posterior a 'hasta'");
        }
        return diaRepositorio.findByDiaBetween(desde, hasta);
    }
 
    /**
     * Obtiene el día correspondiente a una fecha real específica.
     * Permite saber "qué sesión entrené el día X".
     */
    @Transactional(readOnly = true)
    public Dia obtenerPorFecha(Date fecha) throws MyException {
        return diaRepositorio.findByDia(fecha)
                .orElseThrow(() -> new MyException("No se encontró ningún día registrado en la fecha: " + fecha));
    }
 
    /**
     * Obtiene un día por su ID.
     * Lanza excepción si no existe.
     */
    @Transactional(readOnly = true)
    public Dia obtenerPorId(UUID diaId) throws MyException {
        return diaRepositorio.findById(diaId)
                .orElseThrow(() -> new MyException("No se encontró el día con id: " + diaId));
    }
 
    /**
     * Cuenta los días que tiene una semana.
     * Útil para nombrar automáticamente: "Día " + (count + 1).
     */
    @Transactional(readOnly = true)
    public int contarPorSemana(UUID semanaId) throws MyException {
        Semana semana = obtenerSemana(semanaId);
        return diaRepositorio.countBySemana(semana);
    }
 
    // ── Validaciones privadas ──────────────────────────────────────────────────
 
    private void validarNombre(String nombre) throws MyException {
        if (nombre == null || nombre.isBlank()) {
            throw new MyException("El nombre no puede ser nulo ni vacío");
        }
    }
 
    private Semana obtenerSemana(UUID semanaId) throws MyException {
        return semanaRepositorio.findById(semanaId)
                .orElseThrow(() -> new MyException("No se encontró la semana con id: " + semanaId));
    }  
}
