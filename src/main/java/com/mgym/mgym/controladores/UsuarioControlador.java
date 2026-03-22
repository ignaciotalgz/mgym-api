package com.mgym.mgym.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mgym.mgym.servicios.UsuarioServicio;

/**
 * UsuarioControlador
 *
 * Por ahora UsuarioServicio está vacío — este controlador está preparado
 * para recibir los endpoints de registro, login y gestión de usuarios
 * a medida que se implemente la lógica de negocio correspondiente.
 *
 * Endpoints planeados:
 *   POST   /api/usuarios/registro   → registrar nuevo usuario
 *   POST   /api/usuarios/login      → autenticar (futuro JWT)
 *   GET    /api/usuarios/{id}       → obtener perfil
 *   PUT    /api/usuarios/{id}       → modificar datos
 *   DELETE /api/usuarios/{id}       → baja lógica
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    // Los endpoints se agregarán a medida que UsuarioServicio implemente sus métodos.
}
