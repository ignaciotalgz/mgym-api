package com.mgym.mgym.modelos;

import com.mgym.mgym.enumeraciones.Rol;

import lombok.Data;

@Data
public class UsuarioCreateDTO {
    private String nombre;
    private String pass;
    private Rol rol;
}
