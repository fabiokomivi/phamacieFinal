package com.pharmacie.FxControllers.messages;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class AllowMessage {

    boolean allow;

    @FXML
    private Label messageLabel;


    @FXML
    void onCancel(ActionEvent event) {
        allow = false;
        closeMessage(event);
    }

    @FXML
    void onOk(ActionEvent event) {
        allow = true;
        closeMessage(event);
    }


    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public boolean getPermission() {
        return allow;
    }

    void closeMessage(ActionEvent event) {
        // Récupérer le bouton qui a déclenché l'événement
        Node source = (Node) event.getSource();

        // Obtenir la scène et ensuite la fenêtre actuelle
        Stage stage = (Stage) source.getScene().getWindow();

        // Fermer la fenêtre ou le message
        stage.close();
    }

}
