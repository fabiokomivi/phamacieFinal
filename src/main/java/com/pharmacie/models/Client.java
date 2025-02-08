package com.pharmacie.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    private String email;

    @Column(name = "contact", nullable = true, unique = true)
    @Pattern(regexp = "^[0-9]+$", message = "Contact incorrect")
    private String contact;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Purchase> purchases = new ArrayList<>();

    // Constructeurs
    public Client() {}

    public Client(String name, String contact, String email) {
        this.name = name;
        this.contact = contact;
        this.email = email;
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
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return this.contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<Purchase> getPurchases() {
        return this.purchases;
    }

    public void addPurchase(Purchase purchase) {
            this.purchases.add(purchase);
            purchase.setClient(this); 
    }

    public void removePurchase(Purchase purchase) {
            this.purchases.remove(purchase);
            purchase.setClient(null);  // Assurez-vous que la méthode setClient existe dans Purchase
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", contact='" + contact + '\'' +
                ", purchases=" + purchases.size() + " achats" +
                '}';
    }
}
