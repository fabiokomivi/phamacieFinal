package com.pharmacie.FxControllers.cards;

import com.pharmacie.models.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ClientCard {

    Client client = null;

    @FXML
    private HBox card;

    @FXML
    private Label contactLabel;

    @FXML
    private Label nameLabel;

    public Client getClient() {
        return client;
    }

    public void setData(Client client) {
        this.client = client;
        nameLabel.setText(client.getName());
        contactLabel.setText(client.getContact());
    }


    public void toggle() {
        if (card.getStyleClass().contains("selected"))
            card.getStyleClass().remove("selected");
        else
            card.getStyleClass().add("selected");
    }

    public void removeSelection() {
        if (card.getStyleClass().contains("selected"))
            card.getStyleClass().remove("selected");
    }

    public boolean isCardSelected() {
        return (card.getStyleClass().contains("selected"));
    }


}
