package model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Disciplina extends PanacheEntity {

    public String nome;

    @ManyToOne
    public Semestre semestre;
}
