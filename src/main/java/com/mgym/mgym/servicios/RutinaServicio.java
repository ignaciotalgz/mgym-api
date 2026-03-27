package com.mgym.mgym.servicios;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgym.mgym.entidades.Rutina;
import com.mgym.mgym.entidades.Usuario;
import com.mgym.mgym.excepciones.MyException;
import com.mgym.mgym.modelos.RutinaCreateDTO;
import com.mgym.mgym.repositorios.RutinaRepositorio;
import com.mgym.mgym.repositorios.UsuarioRepositorio;

@Service
public class RutinaServicio {
    @Autowired
    private RutinaRepositorio rutinaRepositorio;
 
    @Autowired  // ← CORRECCIÓN: faltaba @Autowired en el servicio original
    private UsuarioRepositorio usuarioRepositorio;
 
    // ── Escritura ──────────────────────────────────────────────────────────────
 
    /**
     * Crea una nueva rutina para el usuario indicado.
     * La rutina nace inactiva (actual = false) y sin baja.
     * Valida que el usuario no tenga ya una rutina activa con ese nombre.
     */
    @Transactional
    public void crearRutina(RutinaCreateDTO rutinaCreateDTO) throws MyException {
        String nombre = rutinaCreateDTO.getNombre();
        validarNombre(nombre);
        Usuario usuario = obtenerUsuario(rutinaCreateDTO.getUsuarioId());
        if (rutinaRepositorio.existsByUsuarioAndNombreIgnoreCaseAndBajaFalse(usuario, nombre)) {
            throw new MyException("Ya existe una rutina activa con el nombre '" + nombre + "' para este usuario");
        }
        Rutina nueva = new Rutina();
        nueva.setNombre(nombre);
        nueva.setActual(false);
        nueva.setBaja(false);
        nueva.setUsuario(usuario);
        rutinaRepositorio.save(nueva);
    }
 
    /**
     * Baja lógica de una rutina.
     * Si era la rutina actual, la desactiva también.
     */
    @Transactional
    public void bajaRutina(UUID id) throws MyException {
        Rutina rutina = obtenerPorId(id);
        rutina.setBaja(true);
        rutina.setActual(false);
        rutinaRepositorio.save(rutina);
    }
 
    /**
     * Define una rutina como la actual del usuario.
     * Antes de marcarla, desactiva cualquier otra rutina actual del mismo usuario.
     * Usa el repositorio en vez de cargar todas las rutinas en memoria
     * (corrige el bug del servicio original con findAll + stream filter).
     */
    @Transactional
    public void definirActual(UUID rutinaId, UUID usuarioId) throws MyException {
        Usuario usuario = obtenerUsuario(usuarioId);
        Rutina rutina = obtenerPorId(rutinaId);
        if (rutina.isBaja()) {
            throw new MyException("No se puede activar una rutina dada de baja");
        }
        // Desactiva la rutina actual si existe
        Optional<Rutina> actualAnterior = rutinaRepositorio.findByUsuarioAndActualTrue(usuario);
        actualAnterior.ifPresent(r -> {
            r.setActual(false);
            rutinaRepositorio.save(r);
        });
        rutina.setActual(true);
        rutinaRepositorio.save(rutina);
    }
 
    // ── Lectura ────────────────────────────────────────────────────────────────
 
    /** Lista todas las rutinas activas (no dadas de baja) de un usuario. */
    @Transactional(readOnly = true)
    public List<Rutina> listarActivasPorUsuario(UUID usuarioId) throws MyException {
        Usuario usuario = obtenerUsuario(usuarioId);
        return rutinaRepositorio.findByUsuarioAndBajaFalse(usuario);
    }
 
    /** Lista todas las rutinas de un usuario, incluyendo las dadas de baja (historial). */
    @Transactional(readOnly = true)
    public List<Rutina> listarTodasPorUsuario(UUID usuarioId) throws MyException {
        Usuario usuario = obtenerUsuario(usuarioId);
        return rutinaRepositorio.findByUsuario(usuario);
    }
 
    /** Lista rutinas dadas de baja de un usuario (auditoría). */
    @Transactional(readOnly = true)
    public List<Rutina> listarDadasDeBajaPorUsuario(UUID usuarioId) throws MyException {
        Usuario usuario = obtenerUsuario(usuarioId);
        return rutinaRepositorio.findByUsuarioAndBajaTrue(usuario);
    }
 
    /**
     * Obtiene la rutina marcada como actual para el usuario.
     * Lanza excepción si no tiene ninguna definida.
     */
    @Transactional(readOnly = true)
    public Rutina obtenerActual(UUID usuarioId) throws MyException {
        Usuario usuario = obtenerUsuario(usuarioId);
        return rutinaRepositorio.findByUsuarioAndActualTrue(usuario)
                .orElseThrow(() -> new MyException("El usuario no tiene ninguna rutina definida como actual"));
    }
 
    /**
     * Búsqueda de rutinas activas por nombre parcial dentro de las de un usuario.
     */
    @Transactional(readOnly = true)
    public List<Rutina> buscarPorNombre(UUID usuarioId, String texto) throws MyException {
        if (texto == null || texto.isBlank()) {
            throw new MyException("El texto de búsqueda no puede estar vacío");
        }
        Usuario usuario = obtenerUsuario(usuarioId);
        return rutinaRepositorio.findByUsuarioAndNombreContainingIgnoreCaseAndBajaFalse(usuario, texto);
    }
 
    /**
     * Obtiene una rutina por su ID.
     * Lanza excepción si no existe.
     */
    @Transactional(readOnly = true)
    public Rutina obtenerPorId(UUID id) throws MyException {
        return rutinaRepositorio.findById(id)
                .orElseThrow(() -> new MyException("No se encontró la rutina con id: " + id));
    }
 
    // ── Validaciones privadas ──────────────────────────────────────────────────
 
    private void validarNombre(String nombre) throws MyException {
        if (nombre == null || nombre.isBlank()) {
            throw new MyException("El nombre no puede ser nulo ni vacío");
        }
    }
 
    private Usuario obtenerUsuario(UUID usuarioId) throws MyException {
        return usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new MyException("No se encontró el usuario con id: " + usuarioId));
    }
}
