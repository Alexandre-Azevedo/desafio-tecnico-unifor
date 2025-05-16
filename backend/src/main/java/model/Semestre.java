package model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Semestre extends PanacheEntity {

    public int numero;

    @ManyToOne
    public Curso curso;

    @OneToMany(mappedBy = "semestre", cascade = CascadeType.ALL)
    public List<Disciplina> disciplinas;
}
