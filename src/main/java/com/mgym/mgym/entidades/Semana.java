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
public class Semana {
    @Id
    @UuidGenerator
    private UUID semanaId;
    private UUID rutinaId;
    private String nombre;
}
