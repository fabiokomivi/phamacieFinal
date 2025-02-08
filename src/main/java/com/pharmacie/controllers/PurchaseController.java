package com.pharmacie.controllers;


import com.pharmacie.models.Purchase;
import com.pharmacie.services.PurchaseService;

import java.util.List;

public class PurchaseController {

    private PurchaseService purchaseService = new PurchaseService();

    // Méthode pour enregistrer un achat
    public void savePurchase(Purchase purchase) {
        purchaseService.save(purchase);
    }

    // Méthode pour récupérer un achat par son ID
    public Purchase getPurchaseById(int id) {
        return purchaseService.getById(id);
    }

    // Méthode pour récupérer tous les achats
    public List<Purchase> getAllPurchases() {
        return purchaseService.getAll();
    }

    // Méthode pour mettre à jour un achat
    public void updatePurchase(Purchase purchase) {
        purchaseService.update(purchase);
    }

    // Méthode pour supprimer un achat par son ID
    public void deletePurchase(int id) {
        purchaseService.delete(id);
    }
}
