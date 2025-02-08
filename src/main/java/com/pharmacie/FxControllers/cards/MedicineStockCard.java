package com.pharmacie.FxControllers.cards;

import java.net.URL;
import java.util.ResourceBundle;

import com.pharmacie.controllers.MedicineController;
import com.pharmacie.models.Medicine;
import com.pharmacie.utilities.OneSelectable;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MedicineStockCard implements Initializable{

    MedicineController medicineController = new MedicineController();
    Medicine medicine = null;

    private OneSelectable listener = null;

    @FXML
    private VBox card;

    @FXML
    private Label medicineCategorieName;

    @FXML
    private Label medicineName;

    @FXML
    private Label medicineStock;

    public void setData(Medicine medicine) {
        this.medicine = medicine;
        medicineName.setText(medicine.getName());
        medicineCategorieName.setText(medicine.getCategory().getName());
        medicineStock.setText(String.valueOf(medicine.getStock()));
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

    public Medicine getMedicine() {
        return medicine;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.card.setOnMouseClicked(event -> {
            if (listener != null) {
                toggle();
                listener.onSelection(this); // Notifier le parent
            }
        });
    }

    public void setListener(OneSelectable listener) {
        this.listener = listener;
    }

    public void update() {
        if (medicine != null) {
            medicine = medicineController.getMedicineById(medicine.getId());
            setData(medicine);
        }
    }

}
