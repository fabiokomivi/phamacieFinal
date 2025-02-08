package com.pharmacie.FxControllers.cards;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import com.pharmacie.models.Supplier;;

public class CategorySupplierCard {

    Supplier supplier = null;

    @FXML
    private CheckBox checker;

    @FXML
    private Label name;

    public void setData(Supplier supplier) {
        this.supplier = supplier;
        name.setText(supplier.getName());
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public boolean isChecked() {
        return checker.isSelected();
    }

    public void check() {
        checker.setSelected(true);
    }

}
