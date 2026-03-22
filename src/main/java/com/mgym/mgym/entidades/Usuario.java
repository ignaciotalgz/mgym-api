package com.mgym.mgym.entidades;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.mgym.mgym.enumeraciones.Rol;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
@Entity
public class Usuario {
    @Id
    @UuidGenerator
    private UUID usuarioId;
    private String nombre;
    private String pass;
    private Rol rol;
    private boolean baja;
}
