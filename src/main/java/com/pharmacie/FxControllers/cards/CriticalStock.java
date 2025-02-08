package com.pharmacie.FxControllers.cards;

import com.pharmacie.models.Medicine;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CriticalStock {

    @FXML
    private Label name;

    @FXML
    private Label quantity;

    @FXML
    private Label threshold;

    @FXML
    private HBox card;

    public void setData(Medicine medicine) {
        name.setText(medicine.getName());
        threshold.setText(String.valueOf(medicine.getThreshold()));
        quantity.setText(String.valueOf(medicine.getStock()));
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
