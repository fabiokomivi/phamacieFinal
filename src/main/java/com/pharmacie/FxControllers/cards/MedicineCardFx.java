package com.pharmacie.FxControllers.cards;

import com.pharmacie.models.Medicine;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MedicineCardFx {

    Medicine medicine = null;

    @FXML
    private VBox card;

    @FXML
    private Label medicinePrice;

    @FXML
    private Label medicineStock;

    @FXML
    private Label medicineName;

    public void setData(Medicine medicine) {
        this.medicine = medicine;
        medicineName.setText(medicine.getName());
        medicinePrice.setText(String.valueOf((int) medicine.getPrice()) + " FCFA");
        medicineStock.setText(String.valueOf(medicine.getStock()));
    }

    public Medicine getMedicine() {
        return medicine;
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
