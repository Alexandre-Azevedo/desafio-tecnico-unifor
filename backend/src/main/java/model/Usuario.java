package model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Usuario extends PanacheEntity {

    public String nome;
    public String email;
    public String username;
    public String senha;

    @Enumerated(EnumType.STRING)
    public Perfil perfil;
}
