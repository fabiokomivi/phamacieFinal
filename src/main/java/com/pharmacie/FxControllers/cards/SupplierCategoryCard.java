package com.pharmacie.FxControllers.cards;

import com.pharmacie.models.Category;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class SupplierCategoryCard {

    Category category = null;

    @FXML
    private Label name;

    @FXML
    private CheckBox checker;

    public void setData(Category category) {
        this.category = category;
        name.setText(category.getName());
    }

    public boolean isChecked() {
        return checker.isSelected();
    }

    public Category getCategory() {
        return category;
    }

    public void check() {
        checker.setSelected(true);
    }

}
