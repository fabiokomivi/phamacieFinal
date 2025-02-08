package com.pharmacie.FxControllers.cards;

import java.time.format.DateTimeFormatter;

import com.pharmacie.models.Purchase;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.SVGPath;

public class DashboardPurchase {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    
    Purchase purchase = null;

    @FXML
    private HBox card;

    @FXML
    private Label hourTF;

    @FXML
    private SVGPath knownStatus;

    @FXML
    private Label nameTF;

    @FXML
    private Label totalTF;

    public void setData(Purchase purchase){
        this.purchase = purchase;
        if(purchase.getClient()==null){
            nameTF.setText("inconnu");
        }
        else{
            nameTF.setText(purchase.getClient().getName());
        }

        hourTF.setText(purchase.getCreatedAt().format(formatter));

        totalTF.setText(String.valueOf(purchase.getTotal() + " FCFA"));

        if(purchase.getStatus()) {
            knownStatus.setContent("M15 19C15 16.7909 12.3137 15 9 15C5.68629 15 3 16.7909 3 19M21 10L17 14L15 12M9 12C6.79086 12 5 10.2091 5 8C5 5.79086 6.79086 4 9 4C11.2091 4 13 5.79086 13 8C13 10.2091 11.2091 12 9 12Z");
            knownStatus.getStyleClass().add("svg-green");
        }
        else {
            knownStatus.setContent("M15 19C15 16.7909 12.3137 15 9 15C5.68629 15 3 16.7909 3 19M17 14L19 12M19 12L21 10M19 12L17 10M19 12L21 14M9 12C6.79086 12 5 10.2091 5 8C5 5.79086 6.79086 4 9 4C11.2091 4 13 5.79086 13 8C13 10.2091 11.2091 12 9 12Z");
            knownStatus.getStyleClass().add("svg-red");
        }

    }

}
