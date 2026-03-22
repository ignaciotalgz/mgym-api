package com.mgym.mgym.servicios;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgym.mgym.MyException;
import com.mgym.mgym.entidades.Dia;
import com.mgym.mgym.entidades.Semana;
import com.mgym.mgym.repositorios.DiaRepositorio;
import com.mgym.mgym.repositorios.SemanaRepositorio;

@Service
public class DiaServicio {
    @Autowired
    private DiaRepositorio diaRepositorio;
    private SemanaRepositorio semanaRepositorio;

    @Transactional
    public void crearDia(String nombre, UUID semanaId) throws MyException{
        Optional<Semana> respuesta = semanaRepositorio.findById(semanaId);
        if(respuesta.isPresent()){
            Semana semana = respuesta.get();
            Dia nuevoDia = new Dia();
            nuevoDia.setNombre(nombre);
            nuevoDia.setSemana(semana);
            diaRepositorio.save(nuevoDia);
        }
        else {
            throw new MyException("No se encontro la semana");
        }
    }
}
