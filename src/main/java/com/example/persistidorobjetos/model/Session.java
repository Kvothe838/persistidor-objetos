package com.example.persistidorobjetos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.TABLE;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
@Entity
@Table(name="session")
public class Session {
    @Id
    @EqualsAndHashCode.Exclude
    private Long id;
    @Column(name="ultimo_acceso")
    private Date ultimoAcceso;
}
