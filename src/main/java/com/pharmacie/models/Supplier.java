package com.pharmacie.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "suppliers")
public class Supplier extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    private String email;

    @Column(name = "contact", nullable = true, unique = true)
    @Pattern(regexp = "^[0-9]+$", message = "Contact incorrect")
    private String contact;

    @OneToMany(mappedBy = "supplier", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private Set<SupplierCategory> categoriesLink = new HashSet<>();

    // constructeur
    public Supplier() {}

    public Supplier(String name, String contact, String email ){
        this.name = name;
        this.contact = contact;
        this.email = email;
    }

    //getters and setters

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return this.contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<SupplierCategory> getCategoriesLink() {
        return this.categoriesLink;
    }

    public void addCategoryLink(SupplierCategory categoryLink) {
        this.categoriesLink.add(categoryLink);
        categoryLink.setSupplier(this);
    }

    public void removeCategoryLink(SupplierCategory categoryLink) {
        this.categoriesLink.remove(categoryLink);
        //categoryLink.setSupplier(null);
    }

    public boolean hasCategory(Category category ) {
        for (SupplierCategory supplierCategory : categoriesLink)
            if (supplierCategory.getCategory().getId() == category.getId())
                return true;
        return false;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", contact='" + contact + '\'' +
                ", purchases=" + this.categoriesLink.size() + " categoris" +
                '}';
    }
}

