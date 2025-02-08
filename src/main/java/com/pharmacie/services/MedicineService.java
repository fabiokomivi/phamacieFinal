package com.pharmacie.services;


import com.pharmacie.dao.MedicineDAO;
import com.pharmacie.models.Medicine;
import java.util.List;

public class MedicineService {

    private MedicineDAO medicineDAO;

    public MedicineService() {
        this.medicineDAO = new MedicineDAO();  // Instancier le DAO
    }

    // Enregistrer un médicament
    public void saveMedicine(Medicine medicine) {
        medicineDAO.save(medicine);
    }

    // Récupérer un médicament par ID
    public Medicine getMedicineById(int id) {
        return medicineDAO.getById(id);
    }

    // Récupérer tous les médicaments
    public List<Medicine> getAllMedicines() {
        return medicineDAO.getAll();
    }

    // Mettre à jour un médicament
    public void updateMedicine(Medicine medicine) {
        medicineDAO.update(medicine);
    }

    // Supprimer un médicament par ID
    public void deleteMedicine(int id) {
        medicineDAO.delete(id);
    }
}
