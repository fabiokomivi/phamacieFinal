package com.pharmacie.FxControllers.pops;

import com.pharmacie.controllers.PaymentModeController;
import com.pharmacie.models.PaymentMode;
import com.pharmacie.utilities.Dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreatePayment {

    PaymentModeController paymentModeController = new PaymentModeController();

    PaymentMode paymentMode = null;

    @FXML
    private TextArea description;

    @FXML
    private TextField name;

    @FXML
    private Label title;

    @FXML
    void onClose(ActionEvent event) {
        closeWindow(event);
    }

    @FXML
    void onCreate(ActionEvent event) {
        String modePayment = name.getText().trim();
        String modePaymentDescription = description.getText().trim();

        if(modePayment.isEmpty())
            Dialogs.showSimpleMessage("nom de mode de paiement invalide");
        else {
            paymentMode = new PaymentMode(modePayment, modePaymentDescription);
            paymentModeController.savePaymentMode(paymentMode);
            closeWindow(event);
        }
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
