package com.pharmacie.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "payment_modes")
public class PaymentMode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @OneToMany(mappedBy = "paymentMode", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Purchase> purchases = new ArrayList<>();

    // Constructeur par défaut
    public PaymentMode() {}

    // Constructeur avec arguments
    public PaymentMode(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void addPurchase(Purchase purchase) {
        this.purchases.add(purchase);
        purchase.setPaymentMode(this);
    }

    public void removePurchase(Purchase purchase) {
        this.purchases.remove(purchase);
        purchase.setPaymentMode(null);
    }

    // Méthode toString pour afficher l'objet
    @Override
    public String toString() {
        return "PaymentMode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", purchasesCount=" + purchases.size() +
                '}';
    }
}
