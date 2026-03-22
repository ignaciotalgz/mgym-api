package com.mgym.mgym.controladores;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mgym.mgym.MyException;
import com.mgym.mgym.servicios.RutinaServicio;

@RestController
@RequestMapping("/api/rutinas")
public class RutinaControlador {

    @Autowired
    private RutinaServicio rutinaServicio;

    // POST /api/rutinas
    @PostMapping
    public ResponseEntity<?> crearRutina(
            @RequestParam String nombre,
            @RequestParam UUID usuarioId) {
        try {
            rutinaServicio.crearRutina(nombre, usuarioId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Rutina creada correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /api/rutinas/{id}  (baja lógica)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> bajaRutina(@PathVariable UUID id) {
        rutinaServicio.bajaRutina(id);
        return ResponseEntity.ok("Rutina dada de baja correctamente");
    }

    // PATCH /api/rutinas/{id}/actual  → define la rutina activa del usuario
    @PatchMapping("/{id}/actual")
    public ResponseEntity<?> definirActual(
            @PathVariable UUID id,
            @RequestParam UUID usuarioId) {
        rutinaServicio.defineActual(id, usuarioId);
        return ResponseEntity.ok("Rutina actual definida correctamente");
    }
}
