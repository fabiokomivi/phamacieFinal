package com.pharmacie.FxControllers.cards;

import com.pharmacie.models.PaymentMode;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class PaymentCard {

    PaymentMode paymentMode = null;

    @FXML
    private VBox card;

    @FXML
    private Label name;


    @FXML
    private ImageView paymentImage;

    public void setData(PaymentMode paymentMode ) {
        this.paymentMode = paymentMode;
        name.setText(paymentMode.getName());

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

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

}
