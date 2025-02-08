package com.pharmacie.services;

import com.pharmacie.dao.MedicinePurchaseDAO;
import com.pharmacie.models.MedicinePurchase;
import java.util.List;

public class MedicinePurchaseService {

    private MedicinePurchaseDAO medicinePurchaseDAO;

    public MedicinePurchaseService() {
        this.medicinePurchaseDAO = new MedicinePurchaseDAO();  // Instancier le DAO
    }

    // Enregistrer un achat de médicament
    public void saveMedicinePurchase(MedicinePurchase medicinePurchase) {
        medicinePurchaseDAO.save(medicinePurchase);
    }

    // Récupérer un achat de médicament par ID
    public MedicinePurchase getMedicinePurchaseById(int id) {
        return medicinePurchaseDAO.getById(id);
    }

    // Récupérer tous les achats de médicaments
    public List<MedicinePurchase> getAllMedicinePurchases() {
        return medicinePurchaseDAO.getAll();
    }

    // Mettre à jour un achat de médicament
    public void updateMedicinePurchase(MedicinePurchase medicinePurchase) {
        medicinePurchaseDAO.update(medicinePurchase);
    }

    // Supprimer un achat de médicament par ID
    public void deleteMedicinePurchase(int id) {
        medicinePurchaseDAO.delete(id);
    }
}
