package com.mgym.mgym.modelos;

import java.util.UUID;

import lombok.Data;

@Data
public class DiaCreateDTO {
    private String nombre;
    private UUID semanaId;
}
