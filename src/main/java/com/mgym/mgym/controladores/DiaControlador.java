package com.mgym.mgym.controladores;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mgym.mgym.MyException;
import com.mgym.mgym.servicios.DiaServicio;

@RestController
@RequestMapping("/api/dias")
public class DiaControlador {

    @Autowired
    private DiaServicio diaServicio;

    // POST /api/dias
    @PostMapping
    public ResponseEntity<?> crearDia(
            @RequestParam String nombre,
            @RequestParam UUID semanaId) {
        try {
            diaServicio.crearDia(nombre, semanaId);
            return ResponseEntity.status(HttpStatus.CREATED).body("Dia creado correctamente");
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
