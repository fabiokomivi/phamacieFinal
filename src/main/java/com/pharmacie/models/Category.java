package com.pharmacie.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = true)
    private String description;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval=true)
    private Set<Medicine> medicines = new HashSet<>();

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Set<SupplierCategory> suppliersLink = new HashSet<>();

    // Constructeur
    public Category() {}

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Constructeur sans description
    public Category(String name) {
        this.name = name;
        this.description = "";  // Par défaut, si la description n'est pas fournie
    }

    // Getters et Setters
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<SupplierCategory> getSuppliersLink() {
        return this.suppliersLink;
    }

    public void addSupplierLink(SupplierCategory supplierLink) {
        this.suppliersLink.add(supplierLink);
        supplierLink.setCategory(this);
    }

    public void removeSupplierLink(SupplierCategory supplierLink) {
        this.suppliersLink.remove(supplierLink);
        //supplierLink.setCategory(null);
    }

    public boolean hasSupplier(Supplier supplier ) {
        for (SupplierCategory supplierCategory : suppliersLink)
            if (supplierCategory.getSupplier().getId() == supplier.getId())
                return true;
        return false;
    }

    public void addMedicine(Medicine medicine) {
        medicines.add(medicine);
    }

    public void removeMedicine(Medicine medicine) {
        medicines.remove(medicine);
    }

    public Set<Medicine> getMedicines() {
        return medicines;
    }

    public boolean hasMedicine(Medicine medicine ) {
        for (Medicine currentMedicine : medicines)
            if (currentMedicine.getId() == medicine.getId())
                return true;
        return false;
    }

    // Méthode toString pour l'affichage de l'objet
    @Override
    public String toString() {
        return "Category{" +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                ", description='" + this.description + '\'' +
                ", medicinesCount=" + this.medicines.size() +  // Affiche le nombre de médicaments associés
                ", suppliersCount=" + this.suppliersLink.size() +  // Affiche le nombre de fournisseurs associés
                '}';
    }
}
