package com.mgym.mgym.controladores;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mgym.mgym.MyException;
import com.mgym.mgym.servicios.SemanaServicio;

@RestController
@RequestMapping("/api/semanas")
public class SemanaControlador {

    @Autowired
    private SemanaServicio semanaServicio;

    // POST /api/semanas
    @PostMapping
    public ResponseEntity<?> crearSemana(
            @RequestParam String nombre,
            @RequestParam UUID rutinaId) {
        try {
            semanaServicio.crearSemana(nombre, rutinaId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Semana creada correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
