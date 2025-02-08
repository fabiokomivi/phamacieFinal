package com.pharmacie.models;

import jakarta.persistence.*;

@Entity
@Table(name = "medicine_purchases")
public class MedicinePurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    // Constructeur
    public MedicinePurchase() {}

    public MedicinePurchase(Purchase purchase, Medicine medicine, int quantity) {
        this.purchase = purchase;
        this.medicine = medicine;
        this.quantity = quantity;
        this.totalPrice = medicine.getPrice() * quantity;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        if(purchase != null) {
            this.purchase = purchase;
        }
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.totalPrice = this.medicine.getPrice() * quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @PrePersist
    void onCreate(){
        this.totalPrice = medicine.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return "MedicinePurchase{" +
                "id=" + this.id +
                ", medicine=" + this.medicine.getName() +
                ", quantity=" + this.quantity +
                ", totalPrice=" + this.totalPrice +
                '}';
    }
}
