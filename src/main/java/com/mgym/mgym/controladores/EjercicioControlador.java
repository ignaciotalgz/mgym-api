package com.mgym.mgym.controladores;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mgym.mgym.MyException;
import com.mgym.mgym.entidades.Ejercicio;
import com.mgym.mgym.servicios.EjercicioServicio;

@RestController
@RequestMapping("/api/ejercicios")
public class EjercicioControlador {

    @Autowired
    private EjercicioServicio ejercicioServicio;

    // POST /api/ejercicios
    @PostMapping
    public ResponseEntity<?> crearEjercicio(
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion) {
        try {
            ejercicioServicio.crearEjercicio(nombre, descripcion);
            return ResponseEntity.status(HttpStatus.CREATED).body("Ejercicio creado correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET /api/ejercicios
    @GetMapping
    public ResponseEntity<List<Ejercicio>> listarEjercicios() {
        List<Ejercicio> ejercicios = ejercicioServicio.listarEjercicios();
        return ResponseEntity.ok(ejercicios);
    }

    // PUT /api/ejercicios/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> modificarEjercicio(
            @PathVariable UUID id,
            @RequestParam String nombre,
            @RequestParam(required = false) String descripcion) {
        try {
            ejercicioServicio.modificarEjercicio(nombre, descripcion, id);
            return ResponseEntity.ok("Ejercicio modificado correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /api/ejercicios/{id}  (baja lógica)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> bajaEjercicio(@PathVariable UUID id) {
        ejercicioServicio.bajaEjercicio(id);
        return ResponseEntity.ok("Ejercicio dado de baja correctamente");
    }
}
