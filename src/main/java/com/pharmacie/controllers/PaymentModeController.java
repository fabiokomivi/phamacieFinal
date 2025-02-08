package com.pharmacie.controllers;


import com.pharmacie.models.PaymentMode;
import com.pharmacie.services.PaymentModeService;

import java.util.List;

public class PaymentModeController {

    private PaymentModeService paymentModeService;

    public PaymentModeController() {
        this.paymentModeService = new PaymentModeService();  // Instancier le service
    }

    // Ajouter un mode de paiement
    public void savePaymentMode(PaymentMode paymentMode) {
        paymentModeService.savePaymentMode(paymentMode);
    }

    // Mettre à jour un mode de paiement
    public void updatePaymentMode(int id, String name, String description) {
        PaymentMode paymentMode = paymentModeService.getPaymentModeById(id);
        if (paymentMode != null) {
            paymentMode.setName(name);
            paymentMode.setDescription(description);
            paymentModeService.updatePaymentMode(paymentMode);
        }
    }

    // Supprimer un mode de paiement
    public void deletePaymentMode(int id) {
        paymentModeService.deletePaymentMode(id);
    }

    // Afficher tous les modes de paiement
    public List<PaymentMode> getAllPaymentModes() {
        return paymentModeService.getAllPaymentModes();
    }

    // Afficher un mode de paiement par ID
    public PaymentMode getPaymentModeById(int id) {
        return paymentModeService.getPaymentModeById(id);
    }
}
