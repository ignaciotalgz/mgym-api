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
import com.mgym.mgym.entidades.Rutina;
import com.mgym.mgym.entidades.Usuario;
import com.mgym.mgym.repositorios.RutinaRepositorio;
import com.mgym.mgym.repositorios.UsuarioRepositorio;

@Service
public class RutinaServicio {
    @Autowired
    private RutinaRepositorio rutinaRepositorio;
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void crearRutina(String nombre, UUID usuarioId) throws MyException{
        validar(nombre);
        Optional<Usuario> user = usuarioRepositorio.findById(usuarioId);
        if(user.isPresent()){
            Usuario usuario = user.get();
            Rutina nuevaRutina = new Rutina();
            nuevaRutina.setNombre(nombre);
            nuevaRutina.setActual(false);
            nuevaRutina.setBaja(false);
            nuevaRutina.setUsuario(usuario);
            rutinaRepositorio.save(nuevaRutina);
        }
        else {
            throw new MyException("No se encontro el usuario");
        }
    }

    @Transactional
    public void bajaRutina(UUID id){
        Optional<Rutina> respuesta = rutinaRepositorio.findById(id);
        if(respuesta.isPresent()){
            Rutina rutina = respuesta.get();
            rutina.setBaja(true);
            rutinaRepositorio.save(rutina);
        }
    }

    @Transactional
    public void defineActual(UUID id, UUID usuarioId){
        Optional<Rutina> respuesta = rutinaRepositorio.findById(id);
        if(respuesta.isPresent()){
            Optional<Usuario> user = usuarioRepositorio.findById(usuarioId);
            if(user.isPresent()){
                Usuario usuario = user.get();
                List<Rutina> rutinas = new ArrayList<>();
                rutinas = rutinaRepositorio.findAll();
                List<Rutina> rutinasUsuario = rutinas.stream().filter((rut) -> rut.getUsuario() == usuario).toList();
                for (Rutina rutina : rutinasUsuario) {
                    rutina.setActual(false);
                }    
                Rutina rutina = respuesta.get();
                rutina.setActual(true);            
                rutinaRepositorio.save(rutina);
            }
        }
    }

    private void validar(String nombre) throws MyException{
        if(nombre.isEmpty() || nombre == null){
            throw new MyException("El nombre no puede ser nulo ni vacio");
        }
    }
}
