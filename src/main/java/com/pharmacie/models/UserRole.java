package com.pharmacie.models;

import jakarta.persistence.*;

@Entity
@Table(
    name = "user_roles",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"})
)
public class UserRole extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true) // FK vers la table `users`
    private User user;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = true) // FK vers la table `roles`
    private Role role;

    // Constructeurs
    public UserRole() {}

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;

        
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", user=" + (user != null ? user.getName() : "null") +
                ", role=" + (role != null ? role.getName() : "null") +
                '}';
    }
}
