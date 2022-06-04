package com.example.persistidorobjetos.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="session")
public class Session {
    @Id
    private int id;
    @Column(name="ultimo_acceso")
    private Date ultimoAcceso;
}
