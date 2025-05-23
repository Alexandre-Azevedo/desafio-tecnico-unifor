package model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Curso extends PanacheEntity {

    public String nome;

    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    public List<Semestre> semestres;
}
