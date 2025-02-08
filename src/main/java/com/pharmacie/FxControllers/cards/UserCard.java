package com.pharmacie.FxControllers.cards;

import com.pharmacie.models.User;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class UserCard {

    User user = null;

    @FXML
    private VBox card;

    @FXML
    private Label contact;

    @FXML
    private Label email;

    @FXML
    private Label name;

    public void setData(User user) {
        this.user = user;
        name.setText(user.getName());
        email.setText(user.getEmail());
        contact.setText(user.getContact());
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

    public User getUser() {
        return user;
    }


}
