package com.example.persistidorobjetos.model;

import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.annotations.Parameter;

import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.GenerationType.TABLE;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name="clase")
public class Clase {
    @Id
	@GeneratedValue(strategy = TABLE)
    private Long id;
    @Column(name="nombre")
    private String nombre;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Atributo> atributos;
}
