package com.mgym.mgym.servicios;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgym.mgym.entidades.Ejercicio;
import com.mgym.mgym.excepciones.MyException;
import com.mgym.mgym.modelos.EjercicioCreateDTO;
import com.mgym.mgym.repositorios.EjercicioRepositorio;

@Service
public class EjercicioServicio {
    @Autowired
    private EjercicioRepositorio ejercicioRepositorio;
 
    // ── Escritura ──────────────────────────────────────────────────────────────
 
    /**
     * Crea un nuevo ejercicio validando que el nombre no esté vacío
     * y que no exista otro ejercicio activo con el mismo nombre.
     */
    @Transactional
    public void crearEjercicio(EjercicioCreateDTO ejercicioCreateDTO) throws MyException {
        String nombre = ejercicioCreateDTO.getNombre();
        validarNombre(nombre);
        if (ejercicioRepositorio.existsByNombreIgnoreCaseAndBajaFalse(nombre)) {
            throw new MyException("Ya existe un ejercicio activo con el nombre: " + nombre);
        }
        Ejercicio nuevo = new Ejercicio();
        nuevo.setNombre(nombre);
        nuevo.setDescripcion(ejercicioCreateDTO.getDescripcion());
        nuevo.setBaja(ejercicioCreateDTO.isBaja());
        ejercicioRepositorio.save(nuevo);
    }
 
    /**
     * Modifica nombre y/o descripción de un ejercicio existente.
     * Si se cambia el nombre, valida que el nuevo no esté ya en uso.
     */
    @Transactional
    public void modificarEjercicio(UUID id, String nombre, String descripcion) throws MyException {
        validarNombre(nombre);
        Ejercicio ejercicio = obtenerPorId(id);
        // Solo valida duplicado si el nombre realmente cambia
        if (!ejercicio.getNombre().equalsIgnoreCase(nombre)
                && ejercicioRepositorio.existsByNombreIgnoreCaseAndBajaFalse(nombre)) {
            throw new MyException("Ya existe un ejercicio activo con el nombre: " + nombre);
        }
        ejercicio.setNombre(nombre);
        ejercicio.setDescripcion(descripcion);
        ejercicioRepositorio.save(ejercicio);
    }
 
    /**
     * Baja lógica: el ejercicio queda en la base para conservar el historial
     * de ejecuciones pasadas, pero no aparece en los listados activos.
     */
    @Transactional
    public void bajaEjercicio(UUID id) throws MyException {
        Ejercicio ejercicio = obtenerPorId(id);
        ejercicio.setBaja(true);
        ejercicioRepositorio.save(ejercicio);
    }
 
    // ── Lectura ────────────────────────────────────────────────────────────────
 
    /** Lista todos los ejercicios activos (no dados de baja). */
    @Transactional(readOnly = true)
    public List<Ejercicio> listarActivos() {
        return ejercicioRepositorio.findByBajaFalse();
    }
 
    /** Lista todos los ejercicios incluyendo los dados de baja (auditoría). */
    @Transactional(readOnly = true)
    public List<Ejercicio> listarTodos() {
        return ejercicioRepositorio.findAll();
    }
 
    /** Lista solo los ejercicios dados de baja (historial). */
    @Transactional(readOnly = true)
    public List<Ejercicio> listarDadosDeBaja() {
        return ejercicioRepositorio.findByBajaTrue();
    }
 
    /**
     * Búsqueda parcial por nombre entre los ejercicios activos.
     * Útil para un campo de búsqueda/autocomplete en el frontend.
     */
    @Transactional(readOnly = true)
    public List<Ejercicio> buscarPorNombre(String texto) throws MyException {
        if (texto == null || texto.isBlank()) {
            throw new MyException("El texto de búsqueda no puede estar vacío");
        }
        return ejercicioRepositorio.findByNombreContainingIgnoreCase(texto);
    }
 
    /**
     * Obtiene un ejercicio activo por su nombre exacto.
     * Lanza excepción si no existe.
     */
    @Transactional(readOnly = true)
    public Ejercicio obtenerPorNombre(String nombre) throws MyException {
        return ejercicioRepositorio.findByNombreIgnoreCaseAndBajaFalse(nombre)
                .orElseThrow(() -> new MyException("No se encontró un ejercicio activo con el nombre: " + nombre));
    }
 
    /**
     * Obtiene un ejercicio por su ID.
     * Lanza excepción si no existe.
     */
    @Transactional(readOnly = true)
    public Ejercicio obtenerPorId(UUID id) throws MyException {
        return ejercicioRepositorio.findById(id)
                .orElseThrow(() -> new MyException("No se encontró el ejercicio con id: " + id));
    }
 
    // ── Validaciones privadas ──────────────────────────────────────────────────
 
    private void validarNombre(String nombre) throws MyException {
        if (nombre == null || nombre.isBlank()) {
            throw new MyException("El nombre no puede ser nulo ni vacío");
        }
    }
}
