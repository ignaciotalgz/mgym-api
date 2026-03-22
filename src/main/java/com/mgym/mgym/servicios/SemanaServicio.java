package com.mgym.mgym.servicios;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgym.mgym.MyException;
import com.mgym.mgym.entidades.Rutina;
import com.mgym.mgym.entidades.Semana;
import com.mgym.mgym.repositorios.RutinaRepositorio;
import com.mgym.mgym.repositorios.SemanaRepositorio;

@Service
public class SemanaServicio {

    @Autowired
    private SemanaRepositorio semanaRepositorio;
    private RutinaRepositorio rutinaRepositorio;

    @Transactional
    public void crearSemana(String nombre, UUID rutinaId) throws MyException{
        Optional<Rutina> respuesta = rutinaRepositorio.findById(rutinaId);
        if(respuesta.isPresent()){
            Rutina rutina = respuesta.get();
            Semana nuevaSemana = new Semana();
            nuevaSemana.setNombre(nombre);
            nuevaSemana.setRutina(rutina);
            semanaRepositorio.save(nuevaSemana);
        }
        else {
            throw new MyException("No se encontro la rutina");
        }
    }
}
