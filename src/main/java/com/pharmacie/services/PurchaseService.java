package com.pharmacie.services;

import com.pharmacie.dao.PurchaseDAO;
import com.pharmacie.models.Purchase;

import java.util.List;

public class PurchaseService {

    private PurchaseDAO purchaseDAO = new PurchaseDAO();

    // Méthode pour enregistrer un achat
    public void save(Purchase purchase) {
        purchaseDAO.save(purchase);
    }

    // Méthode pour récupérer un achat par son ID
    public Purchase getById(int id) {
        return purchaseDAO.getById(id);
    }

    // Méthode pour récupérer tous les achats
    public List<Purchase> getAll() {
        return purchaseDAO.getAll();
    }

    // Méthode pour mettre à jour un achat existant
    public void update(Purchase purchase) {
        purchaseDAO.update(purchase);
    }

    // Méthode pour supprimer un achat par son ID
    public void delete(int id) {
        purchaseDAO.delete(id);
    }
}
