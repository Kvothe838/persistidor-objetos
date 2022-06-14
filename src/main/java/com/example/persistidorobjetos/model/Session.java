package com.example.persistidorobjetos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.TABLE;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="session")
@Data
public class Session {
    @Id
    private Long id;
    @Column(name="ultimo_acceso")
    private Date ultimoAcceso;
}
