package com.mgym.mgym.entidades;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Dia {
    @Id
    @UuidGenerator
    private UUID diaId;
    @ManyToOne
    private UUID semanaId;
    private String nombre;
    @Temporal(TemporalType.DATE)
    private Date dia;
}
