package com.pharmacie.controllers;


import com.pharmacie.models.SupplierCategory;
import com.pharmacie.services.SupplierCategoryService;

import java.util.List;

public class SupplierCategoryController {

    private SupplierCategoryService supplierCategoryService;

    public SupplierCategoryController() {
        this.supplierCategoryService = new SupplierCategoryService();
    }

    // Méthode pour enregistrer une relation fournisseur-catégorie
    public void saveSupplierCategory(SupplierCategory supplierCategory) {
        supplierCategoryService.saveSupplierCategory(supplierCategory);
    }

    // Méthode pour récupérer une relation fournisseur-catégorie par son ID
    public SupplierCategory getSupplierCategoryById(int id) {
        return supplierCategoryService.getSupplierCategoryById(id);
    }

    // Méthode pour récupérer toutes les relations fournisseur-catégorie
    public List<SupplierCategory> getAllSupplierCategories() {
        return supplierCategoryService.getAllSupplierCategories();
    }

    // Méthode pour mettre à jour une relation fournisseur-catégorie
    public void updateSupplierCategory(SupplierCategory supplierCategory) {
        supplierCategoryService.updateSupplierCategory(supplierCategory);
    }

    // Méthode pour supprimer une relation fournisseur-catégorie par son ID
    public void deleteSupplierCategory(int id) {
        supplierCategoryService.deleteSupplierCategory(id);
    }
}
