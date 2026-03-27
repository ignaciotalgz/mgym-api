package com.mgym.mgym.modelos;

import java.util.UUID;

import lombok.Data;

@Data
public class RutinaCreateDTO {
    private String nombre;
    private UUID usuarioId;
}
