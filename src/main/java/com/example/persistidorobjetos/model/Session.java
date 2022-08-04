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
    private int id;
    @Column(name="sId")
    private long sId;
    @Column(name = "ultimo_acceso")
    private Date ultimoAcceso;
}
