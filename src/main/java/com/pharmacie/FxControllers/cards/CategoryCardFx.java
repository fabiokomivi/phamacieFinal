package com.pharmacie.FxControllers.cards;


import com.pharmacie.models.Category;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CategoryCardFx {

    Category category = null;

    @FXML
    private VBox card;

    @FXML
    private Label categoryName;

    @FXML
    private Label medecinesNumber;

    @FXML
    private Label suppliersNumber;

    public void setData(Category category) {
        this.category = category;
        categoryName.setText(category.getName());
        suppliersNumber.setText(String.valueOf(category.getSuppliersLink().size()));
        medecinesNumber.setText(String.valueOf(category.getMedicines().size()));
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

    public Category getCategory() {
        return category;
    }

    public VBox getCard() {
        return card;
    }
}
