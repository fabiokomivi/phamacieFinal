package com.pharmacie.models;


import jakarta.persistence.*;

@Entity
@Table(
    name="supplier_categories",
    uniqueConstraints = @UniqueConstraint(columnNames = {"supplier_id", "category_id"})
)
public class SupplierCategory extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Version
    private int version;  // Version utilisée pour l'optimistic locking

    //constructeur

    public SupplierCategory() {}

    public SupplierCategory(Supplier supplier, Category category) {
        this.category = category;
        this.supplier = supplier;
    }

    // getters and setters

    public int getId() {
        return this.id;
    }

    public Supplier getSupplier() {
        return this.supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "SupplierCategory{" +
               "id=" + id +
               ", supplier=" + (supplier != null ? supplier.getName() : "null") +
               ", category=" + (category != null ? category.getName() : "null") +
               '}';
    }
    
}
