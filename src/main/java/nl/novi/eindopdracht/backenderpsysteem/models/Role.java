package nl.novi.eindopdracht.backenderpsysteem.models;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    private String role;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
