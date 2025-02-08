package com.pharmacie.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stocks")
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "man_date", nullable = false)
    private LocalDateTime manufactureDate;

    @Column(name = "exp_date", nullable = false)
    private LocalDateTime ExpiryDate;

    @Column(name = "acq_date", nullable = false)
    private LocalDateTime AcquisitionDate;

    // Constructeurs
    public Stock() {}

    public Stock(Medicine medicine, int quantity, LocalDateTime dateManufacture, LocalDateTime dateExpiry, LocalDateTime dateAcquisition) {
        this.medicine = medicine;
        this.quantity = quantity;
        this.manufactureDate = dateManufacture;
        this.ExpiryDate = dateExpiry;
        this.AcquisitionDate = dateAcquisition;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    }

    public LocalDateTime getDateManufacture() {
        return manufactureDate;
    }

    public void setDateManufacture(LocalDateTime dateManufacture) {
        this.manufactureDate = dateManufacture;
    }

    public LocalDateTime getDateExpiry() {
        return ExpiryDate;
    }

    public void setDateExpiry(LocalDateTime dateExpiry) {
        this.ExpiryDate = dateExpiry;
    }

    public LocalDateTime getDateAcquisition() {
        return AcquisitionDate;
    }

    public void setDateAcquisition(LocalDateTime dateAcquisition) {
        this.AcquisitionDate = dateAcquisition;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", medicine=" + medicine.getName() +
                ", quantity=" + quantity +
                ", dateManufacture=" + manufactureDate +
                ", dateExpiry=" + ExpiryDate +
                ", dateAcquisition=" + AcquisitionDate +
                '}';
    }
}
