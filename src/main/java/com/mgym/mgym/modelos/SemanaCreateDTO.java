package com.mgym.mgym.modelos;

import java.util.UUID;

import lombok.Data;

@Data
public class SemanaCreateDTO {
    private String nombre;
    private UUID rutinaId;
}
