package com.pharmacie.services;

import com.pharmacie.dao.SupplierCategoryDAO;
import com.pharmacie.models.SupplierCategory;

import java.util.List;

public class SupplierCategoryService {

    private SupplierCategoryDAO supplierCategoryDAO;

    public SupplierCategoryService() {
        this.supplierCategoryDAO = new SupplierCategoryDAO();
    }

    // Méthode pour enregistrer une relation fournisseur-catégorie
    public void saveSupplierCategory(SupplierCategory supplierCategory) {
        supplierCategoryDAO.save(supplierCategory);
    }

    // Méthode pour récupérer une relation fournisseur-catégorie par son ID
    public SupplierCategory getSupplierCategoryById(int id) {
        return supplierCategoryDAO.getById(id);
    }

    // Méthode pour récupérer toutes les relations fournisseur-catégorie
    public List<SupplierCategory> getAllSupplierCategories() {
        return supplierCategoryDAO.getAll();
    }

    // Méthode pour mettre à jour une relation fournisseur-catégorie
    public void updateSupplierCategory(SupplierCategory supplierCategory) {
        supplierCategoryDAO.update(supplierCategory);
    }

    // Méthode pour supprimer une relation fournisseur-catégorie par son ID
    public void deleteSupplierCategory(int id) {
        supplierCategoryDAO.delete(id);
    }
}
