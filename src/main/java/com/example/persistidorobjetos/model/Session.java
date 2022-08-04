package com.example.persistidorobjetos.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="session")
@Data
public class Session {
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
    private long id;
    @Column(name = "ultimo_acceso")
    private Date ultimoAcceso;
}
