package com.pharmacie.FxControllers.messages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SimpleMessage {

    @FXML
    private Label messageLabel;

    @FXML
    private void onOk(ActionEvent event) {
        // Obtenir la fenêtre actuelle à partir du bouton qui a déclenché l'événement
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close(); // Fermer la fenêtre
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }
}
