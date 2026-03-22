package com.mgym.mgym.entidades;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
@Entity
public class Ejercicio {
    @Id
    @UuidGenerator
    private UUID ejercicioId;
    private String nombre;
    private String descripcion;
    private boolean baja;
}
