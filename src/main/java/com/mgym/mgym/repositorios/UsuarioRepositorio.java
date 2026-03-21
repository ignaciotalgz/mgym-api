package com.mgym.mgym.repositorios;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mgym.mgym.entidades.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, UUID>{
@Query("SELECT l FROM Usuario WHERE l.usuarioId = :usuarioId")
    public Usuario buscarUsuariobyId(@Param("usuarioId") UUID usuarioId);
}
