package com.mgym.mgym.servicios;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgym.mgym.MyException;
import com.mgym.mgym.entidades.Ejercicio;
import com.mgym.mgym.repositorios.EjercicioRepositorio;

@Service
public class EjercicioServicio {
    @Autowired
    private EjercicioRepositorio ejercicioRepositorio;

    @Transactional
    public void crearEjercicio(String nombre, String descripcion) throws MyException{
        validar(nombre);
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

    @Transactional
    public void modificarEjercicio(String nombre, String descripcion, UUID id) throws MyException{
        validar(nombre);
        Optional<Ejercicio> respuesta = ejercicioRepositorio.findById(id);
        if(respuesta.isPresent()){
            Ejercicio ejercicio = respuesta.get();
            ejercicio.setNombre(nombre);
            ejercicio.setDescripcion(descripcion);
            ejercicioRepositorio.save(ejercicio);
        }
    }

    @Transactional
    public void bajaEjercicio(UUID id){
        Optional<Ejercicio> respuesta = ejercicioRepositorio.findById(id);
        if(respuesta.isPresent()){
            Ejercicio ejercicio = respuesta.get();
            ejercicio.setBaja(true);
            ejercicioRepositorio.save(ejercicio);
        }
    }

    private void validar(String nombre) throws MyException{
        if(nombre.isEmpty() || nombre == null){
            throw new MyException("El nombre no puede ser nulo ni vacio");
        }
    }
}
