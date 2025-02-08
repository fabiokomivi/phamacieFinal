package com.pharmacie.FxControllers.cards;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import com.pharmacie.models.Supplier;

public class SupplierCardFx {

    Supplier supplier;

    @FXML
    private VBox card;

    @FXML
    private Label email;

    @FXML
    private Label name;

    @FXML
    private Label contact;

    public void setData(Supplier supplier) {
        this.supplier = supplier;
        name.setText(supplier.getName());
        email.setText(supplier.getEmail());
        contact.setText(supplier.getContact());
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

    public Supplier getSupplier() {
        return supplier;
    }
}
