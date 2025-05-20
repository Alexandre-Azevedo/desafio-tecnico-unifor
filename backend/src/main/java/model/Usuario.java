package model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class Usuario extends PanacheEntity {

    public String id;
    public String firstName;
    public String lastName;
    public String email;
    public String username;
    public String password;

    @Enumerated(EnumType.STRING)
    public RealmRoles realmRoles;
}
