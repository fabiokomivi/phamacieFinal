package com.pharmacie.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "logins")
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "started_at", nullable = false, updatable = false)
    private LocalDateTime startedAt;

    @Column(name = "ended_at", nullable = true)
    private LocalDateTime endedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructeur
    public Login() {}

    public Login(User user) {
        this.user = user;
        this.startedAt = LocalDateTime.now();
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt() {
        this.endedAt = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Méthode pour définir la date de fin au moment de la déconnexion
    public void logout() {
        this.endedAt = LocalDateTime.now();
    }

    // toString() pour afficher un résumé de la session
    @Override
    public String toString() {
        return "Login{" +
                "id=" + id +
                ", startedAt=" + startedAt +
                ", endedAt=" + (endedAt != null ? endedAt : "Not yet ended") +
                ", user=" + (user != null ? user.getName() : "No User") +
                '}';
    }
}
