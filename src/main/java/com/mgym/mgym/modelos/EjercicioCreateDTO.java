package com.mgym.mgym.modelos;

import java.util.UUID;

import lombok.Data;

@Data
public class EjercicioCreateDTO {
    private UUID ejercicioId;
    private String nombre;
    private String descripcion;
    private boolean baja;
}
