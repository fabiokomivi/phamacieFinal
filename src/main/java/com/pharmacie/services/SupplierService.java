package com.pharmacie.services;

import com.pharmacie.dao.SupplierDAO;
import com.pharmacie.models.Supplier;

import java.util.List;

public class SupplierService {

    private SupplierDAO supplierDAO;

    public SupplierService() {
        this.supplierDAO = new SupplierDAO();
    }

    // Méthode pour enregistrer un fournisseur
    public void saveSupplier(Supplier supplier) {
        supplierDAO.save(supplier);
    }

    // Méthode pour récupérer un fournisseur par son ID
    public Supplier getSupplierById(int id) {
        return supplierDAO.getById(id);
    }

    // Méthode pour récupérer tous les fournisseurs
    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAll();
    }

    // Méthode pour mettre à jour un fournisseur
    public void updateSupplier(Supplier supplier) {
        supplierDAO.update(supplier);
    }

    // Méthode pour supprimer un fournisseur par son ID
    public void deleteSupplier(int id) {
        supplierDAO.delete(id);
    }

    public Supplier refresh(int id) {
        return supplierDAO.refresh(id);
    }
}
