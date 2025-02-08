package com.pharmacie.FxControllers.cards;

import java.net.URL;
import java.util.ResourceBundle;

import com.pharmacie.models.Category;
import com.pharmacie.utilities.OneSelectable;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class MedicineCategoryCard implements Initializable{

    Category category = null;

    private OneSelectable listener;

    @FXML
    private CheckBox checker;

    @FXML
    private Label name;

    public void setData(Category category) {
        this.category = category;
        name.setText(category.getName());
    }

    public void check() {
        checker.setSelected(true);
    }

    public void uncheck() {
        checker.setSelected(false);
    }

    public Category getCategory() {
        return category;
    }

    public boolean isChecked() {
        return checker.isSelected();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.checker.setOnAction(event -> {
            if (listener != null && checker.isSelected()) {
                listener.onSelection(this); // Notifier le parent
            }
        });
    }

    public void setOnCategorySelectedListener(OneSelectable listener) {
        this.listener = listener;
    }
}
