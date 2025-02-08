package com.pharmacie.services;


import com.pharmacie.dao.PaymentModeDAO;
import com.pharmacie.models.PaymentMode;
import java.util.List;

public class PaymentModeService {

    private PaymentModeDAO paymentModeDAO;

    public PaymentModeService() {
        this.paymentModeDAO = new PaymentModeDAO();  // Instancier le DAO
    }

    // Enregistrer un mode de paiement
    public void savePaymentMode(PaymentMode paymentMode) {
        paymentModeDAO.save(paymentMode);
    }

    // Récupérer un mode de paiement par ID
    public PaymentMode getPaymentModeById(int id) {
        return paymentModeDAO.getById(id);
    }

    // Récupérer tous les modes de paiement
    public List<PaymentMode> getAllPaymentModes() {
        return paymentModeDAO.getAll();
    }

    // Mettre à jour un mode de paiement
    public void updatePaymentMode(PaymentMode paymentMode) {
        paymentModeDAO.update(paymentMode);
    }

    // Supprimer un mode de paiement par ID
    public void deletePaymentMode(int id) {
        paymentModeDAO.delete(id);
    }
}
