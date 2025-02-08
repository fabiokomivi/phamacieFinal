package com.pharmacie.controllers;


import com.pharmacie.models.Medicine;
import com.pharmacie.services.MedicineService;

import java.util.List;

public class MedicineController {

    private MedicineService medicineService;

    public MedicineController() {
        this.medicineService = new MedicineService();  // Instancier le service
    }

    // Ajouter un médicament
    public void saveMedicine(Medicine medicine) {
        medicineService.saveMedicine(medicine);
    }

    // Mettre à jour un médicament
    public void updateMedicine(Medicine medicine) {
        if (medicine != null) {
            medicineService.updateMedicine(medicine);
        }
    }

    // Supprimer un médicament
    public void deleteMedicine(int id) {
        medicineService.deleteMedicine(id);
    }

    // Afficher tous les médicaments
    public List<Medicine> getAllMedicines() {
        return medicineService.getAllMedicines();
    }

    // Afficher un médicament par ID
    public Medicine getMedicineById(int id) {
        return medicineService.getMedicineById(id);
    }
}
