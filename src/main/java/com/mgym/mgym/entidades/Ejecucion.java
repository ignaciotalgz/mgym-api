package com.mgym.mgym.entidades;

import java.time.Period;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import com.mgym.mgym.enumeraciones.EjecucionTipo;

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
public class Ejecucion {
    @Id
    @UuidGenerator
    private UUID ejecucionId;
    @ManyToOne
    private UUID ejercicioId;
    @ManyToOne
    private UUID diaId;
    private EjecucionTipo tipo;
    private float peso;
    private Period tiempo;
    private int dificultad;
}
