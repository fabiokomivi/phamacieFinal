package com.pharmacie.FxControllers.cards;

import com.pharmacie.models.Medicine;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class MedicinePurchasedCard {

    Medicine medicine = null;
    int quantitySelected = 0;

    @FXML
    private HBox card;

    @FXML
    private Label medicineName;

    @FXML
    private Label medicinePrice;

    @FXML
    private Label medicineQuantity;

    public void setData(Medicine medicine) {
        this.medicine = medicine;
        medicineName.setText(medicine.getName());
        medicinePrice.setText(String.valueOf(medicine.getPrice()));
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setQuantitySelected(int quantitySelected) {
        medicineQuantity.setText(String.valueOf(quantitySelected));

        this.quantitySelected = quantitySelected;
    }

    public int getQuantitySelected() {
        return quantitySelected;
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
