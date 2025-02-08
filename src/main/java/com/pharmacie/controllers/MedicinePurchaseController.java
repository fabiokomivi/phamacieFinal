package com.pharmacie.controllers;


import com.pharmacie.models.MedicinePurchase;
import com.pharmacie.services.MedicinePurchaseService;

import java.util.List;

public class MedicinePurchaseController {

    private MedicinePurchaseService medicinePurchaseService;

    public MedicinePurchaseController() {
        this.medicinePurchaseService = new MedicinePurchaseService();  // Instancier le service
    }

    // Ajouter un achat de médicament
    public void saveMedicinePurchase(MedicinePurchase medicinePurchase) {
        medicinePurchaseService.saveMedicinePurchase(medicinePurchase);
    }

    // Mettre à jour un achat de médicament
    public void updateMedicinePurchase( MedicinePurchase medicinePurchase) {
            medicinePurchaseService.updateMedicinePurchase(medicinePurchase);
        }

    // Supprimer un achat de médicament
    public void deleteMedicinePurchase(int id) {
        medicinePurchaseService.deleteMedicinePurchase(id);
    }

    // Afficher tous les achats de médicaments
    public List<MedicinePurchase> getAllMedicinePurchases() {
        return medicinePurchaseService.getAllMedicinePurchases();
    }

    // Afficher un achat de médicament par ID
    public MedicinePurchase getMedicinePurchaseById(int id) {
        return medicinePurchaseService.getMedicinePurchaseById(id);
    }
}
