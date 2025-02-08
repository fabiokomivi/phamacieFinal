package com.pharmacie.FxControllers.cards;

import java.time.format.DateTimeFormatter;

import com.pharmacie.models.Purchase;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class PurchaseCard {

    Purchase purchase = null;

    @FXML
    private HBox card;

    @FXML
    private Label clientName;

    @FXML
    private Label purchaseDate;

    @FXML
    private Label statusTF;

    @FXML
    private Label totalTF;

    public void setData(Purchase purchase) {
        
        this.purchase = purchase;
        if(purchase.getClient() == null) {
            clientName.setText("inconnu");
        } else {
            clientName.setText(purchase.getClient().getName());
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
        purchaseDate.setText(purchase.getCreatedAt().format(formatter));

        if(purchase.getStatus())
            statusTF.setText("validé");
        else
            statusTF.setText("en cours");

        totalTF.setText(String.valueOf(purchase.getTotal()));
    }

    public Purchase getPurchase() {
        return purchase;
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
