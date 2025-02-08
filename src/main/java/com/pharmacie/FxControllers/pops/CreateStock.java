package com.pharmacie.FxControllers.pops;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.pharmacie.controllers.StockController;
import com.pharmacie.models.Medicine;
import com.pharmacie.models.Stock;
import com.pharmacie.utilities.Dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateStock {

    StockController stockController = new StockController();

    Medicine medicine = null;
    Stock stock = null;

    @FXML
    private DatePicker expiryDate;

    @FXML
    private DatePicker manufactureDate;

    @FXML
    private TextField stockQuantity;

    @FXML
    private Label title;

    @FXML
    void addStock(ActionEvent event) {
        try {
            int quantity = Integer.parseInt(stockQuantity.getText().trim());
            LocalDate manufacture = manufactureDate.getValue();
            LocalDate expiry = expiryDate.getValue();

            if(quantity==0){
                Dialogs.showSimpleMessage("la quantité doit etre superieur a 0");
                return;
            }
            if(manufacture == null){
                Dialogs.showSimpleMessage("veuiller entrer la date de fabrication");
                return;
            }

            if(expiry == null){
                Dialogs.showSimpleMessage("veuiller entrer la date d'expiration");
                return;
            }

            if(manufacture.isAfter(expiry)) {
                Dialogs.showSimpleMessage("la date de fabrication doit etre inferieur a la date d'expiration");
                return;
            }

            if (stock==null) {
                stock = new Stock(medicine, quantity, manufacture.atStartOfDay(), expiry.atStartOfDay(), LocalDateTime.now());
                stockController.saveStock(stock);
            } else {
                stock.setDateManufacture(manufacture.atStartOfDay());
                stock.setDateExpiry(expiry.atStartOfDay());
                stock.setQuantity(quantity);
                stockController.updateStock(stock);
            }
            closeWindow(event);
        } catch (NumberFormatException e) {
            //e.printStackTrace();
            Dialogs.showSimpleMessage("quantité invalide");
            return;
        }
    }

    @FXML
    void onClose(ActionEvent event) {
        closeWindow(event);
    }

    public void setData(Medicine medicine) {
        this.medicine = medicine;
        title.setText(medicine.getName());
    }

    public void setData(Stock stock) {
        this.stock = stock;
    }

    public Stock getStock() {
        return stock;
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
