package com.mgym.mgym.servicios;
import java.time.Period;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mgym.mgym.MyException;
import com.mgym.mgym.entidades.Dia;
import com.mgym.mgym.entidades.Ejecucion;
import com.mgym.mgym.entidades.Ejercicio;
import com.mgym.mgym.enumeraciones.EjecucionTipo;
import com.mgym.mgym.repositorios.DiaRepositorio;
import com.mgym.mgym.repositorios.EjecucionRepositorio;
import com.mgym.mgym.repositorios.EjercicioRepositorio;

@Service
public class EjecucionServicio {
    @Autowired
    private EjecucionRepositorio ejecucuionRepositorio;
    private EjercicioRepositorio ejercicioRepositorio;
    private DiaRepositorio diaRepositorio;

    @Transactional
    public void crearEjecucion(UUID ejercicioId, UUID diaId, EjecucionTipo tipo, int dificultad, int repeticionesObjetivo) throws MyException{
        Optional<Ejercicio> ejercicioRespuesta = ejercicioRepositorio.findById(ejercicioId);
        Optional<Dia> diaRespuesta = diaRepositorio.findById(diaId);
        if(ejercicioRespuesta.isPresent() && diaRespuesta.isPresent()){
            Ejercicio ejercicio = ejercicioRespuesta.get();
            Dia dia = diaRespuesta.get();
            Ejecucion ejecucion = new Ejecucion();
            ejecucion.setEjercicio(ejercicio);
            ejecucion.setDia(dia);
            ejecucion.setTipo(tipo);
            ejecucion.setDificultad(dificultad);
            ejecucion.setRepeticionesObjetivo(repeticionesObjetivo);
            ejecucuionRepositorio.save(ejecucion);
        }
        else if(!ejercicioRespuesta.isPresent()){
            throw new MyException("El ejercicio no esta definido");
        }
        else if(!diaRespuesta.isPresent()){
            throw new MyException("El dia no esta definido");
        }
    }

    @Transactional 
    public void ejecutar(UUID ejecucionId, float peso, int dificultad, int repeticionesLogradas, Period tiempo){
        Optional<Ejecucion> respuesta = ejecucuionRepositorio.findById(ejecucionId);
        if(respuesta.isPresent()){
            Ejecucion ejecucion = respuesta.get();
            ejecucion.setPeso(peso);
            ejecucion.setDificultad(dificultad);
            ejecucion.setRepeticionesLogradas(repeticionesLogradas);
            ejecucion.setTiempo(tiempo);
            ejecucuionRepositorio.save(ejecucion);
        }
    }
}
