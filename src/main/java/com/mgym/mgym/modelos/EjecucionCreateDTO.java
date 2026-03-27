package com.mgym.mgym.modelos;

import java.util.UUID;

import com.mgym.mgym.enumeraciones.EjecucionTipo;

import lombok.Data;
@Data
public class EjecucionCreateDTO {
    private UUID ejecucionId;
    private UUID ejercicioId;
    private UUID diaId;
    private EjecucionTipo tipo;
    private int dificultad;
    private int repeticionesObjetivo;
}
