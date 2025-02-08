package com.pharmacie.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.util.HashSet;
import java.util.Set;

import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "users")
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    private String email;

    @Column(name = "contact", nullable = true, unique = true)
    @Pattern(regexp = "^[0-9]+$", message = "Contact incorrect")
    private String contact;

    @Column(name = "active", nullable = false) // Pour suspension
    private boolean active;

    //@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    //private Set<Role> roles = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)  // Relation avec Category
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Set<Purchase> purchases = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @OrderBy("started_at ASC")
    private Set<Login> logins = new HashSet<>();

    // Constructeurs
    public User() {}

    public User(String name, String password, String contact, String email, boolean active) {
        this.name = name;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt(12));
        this.contact = contact;
        this.email = email;
        this.active = active;
    }

    // Getters et Setters

    public int getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String name) {
        this.name = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Set<Login> getLogins() {
        return logins;
    }

    public void addLogin(Login login) {
        logins.add(login);
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public boolean passwordBelongToMe(String password) {
        return (BCrypt.checkpw(password, this.password));
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
        role.addUser(this);
    }

    public boolean isRoot() {
        return role.getName().equals("root");
    }

    public Set<Purchase> getPurchases() {
        return purchases;
    }

    public void addPurchase(Purchase purchase) {
        this.purchases.add(purchase);
        purchase.setUser(this);
    }

    public void removePurchase(Purchase purchase) {
        this.purchases.remove(purchase);
        purchase.setUser(null);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", contact='" + contact + '\'' +
                ",role='" + role.getName() + '\''+
                ", purchases=" + purchases.size() + " achats" +
                '}';
    }
}
