package com.mgym.mgym.entidades;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Rutina {
    @Id
    @UuidGenerator
    private UUID rutinaId;
    @ManyToOne
    private UUID usuarioId;
    private String nombre;
}
