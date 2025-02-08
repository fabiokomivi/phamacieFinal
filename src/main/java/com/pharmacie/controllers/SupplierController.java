package com.pharmacie.controllers;


import com.pharmacie.models.Supplier;
import com.pharmacie.services.SupplierService;

import java.util.List;

public class SupplierController {

    private SupplierService supplierService;

    public SupplierController() {
        this.supplierService = new SupplierService();
    }

    // Méthode pour enregistrer un fournisseur
    public void saveSupplier(Supplier supplier) {
        supplierService.saveSupplier(supplier);
    }

    // Méthode pour récupérer un fournisseur par son ID
    public Supplier getSupplierById(int id) {
        return supplierService.getSupplierById(id);
    }

    // Méthode pour récupérer tous les fournisseurs
    public List<Supplier> getAllSuppliers() {
        return supplierService.getAllSuppliers();
    }

    // Méthode pour mettre à jour un fournisseur
    public void updateSupplier(Supplier supplier) {
        supplierService.updateSupplier(supplier);
    }

    // Méthode pour supprimer un fournisseur par son ID
    public  boolean deleteSupplier(int id) {
        try {
            supplierService.deleteSupplier(id);
            return true; 
        } catch (Exception e) {
            return false;
        }
    }

    public Supplier refresh(int id) {
        return supplierService.refresh(id);
    }
}

