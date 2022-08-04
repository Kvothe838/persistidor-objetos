package com.example.persistidorobjetos.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "valorAtributo")
public class ValorAtributo {
    @Id
    @GeneratedValue(strategy= GenerationType.TABLE)
    private int id;
    @ElementCollection
    private List<String> valores;
    @ManyToOne
    private Instancia instancia;

    public void setValor(String valor){
        this.valores = new ArrayList<>();
        this.valores.add(valor);
    }
}
