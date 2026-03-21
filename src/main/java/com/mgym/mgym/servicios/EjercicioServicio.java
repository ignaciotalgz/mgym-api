package com.mgym.mgym.servicios;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgym.mgym.entidades.Ejercicio;
import com.mgym.mgym.repositorios.EjercicioRepositorio;

@Service
public class EjercicioServicio {
    @Autowired
    private EjercicioRepositorio ejercicioRepositorio;

    @Transactional
    public void crearEjercicio(String nombre, String descripcion){
        Ejercicio nuevoEjercicio = new Ejercicio();
        nuevoEjercicio.setNombre(nombre);
        nuevoEjercicio.setDescripcion(descripcion);
        ejercicioRepositorio.save(nuevoEjercicio);
    }

    @Transactional(readOnly = true)
    public List<Ejercicio> listarEjercicios() {
        List<Ejercicio> ejercicios = new ArrayList<>();
        ejercicios = ejercicioRepositorio.findAll();
        return ejercicios;
    }
}
