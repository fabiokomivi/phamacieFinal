package com.pharmacie.FxControllers.cards;

import java.time.format.DateTimeFormatter;

import com.pharmacie.models.Stock;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class StockCard{

    Stock stock = null;

    @FXML
    private HBox card;

    @FXML
    private Label expiryDate;

    @FXML
    private Label gotDate;

    @FXML
    private Label maufatureDate;

    @FXML
    private Label stockQuantity;

    public void setData(Stock stock) {
        this.stock = stock;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        stockQuantity.setText(String.valueOf(stock.getQuantity()));
        gotDate.setText(stock.getDateAcquisition().format(formatter));
        maufatureDate.setText(stock.getDateManufacture().format(formatter));
        expiryDate.setText(stock.getDateExpiry().format(formatter));
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

    public Stock getStock() {
        return stock;
    }

}
