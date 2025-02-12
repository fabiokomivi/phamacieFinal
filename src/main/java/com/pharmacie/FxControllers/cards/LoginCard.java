package com.pharmacie.FxControllers.cards;

import java.time.format.DateTimeFormatter;

import com.pharmacie.models.Login;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class LoginCard {

    @FXML
    private HBox card;

    @FXML
    private Label end;

    @FXML
    private Label start;

    public void setData(Login login) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");

        start.setText(login.getStartedAt().format(formatter));
        end.setText((login.getEndedAt() == null)?"en cours" : login.getEndedAt().format(formatter));        
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
