package com.pharmacie.FxControllers.topSections;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import com.pharmacie.FxControllers.cards.LoginCard;
import com.pharmacie.models.Login;
import com.pharmacie.models.User;
import com.pharmacie.session.SessionUtil;
import com.pharmacie.utilities.Dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class Parametre implements Initializable{

    Set<Login> loginList = SessionUtil.getCurrentUser().getLogins();

    User user = SessionUtil.getCurrentUser();

    @FXML
    private TextField confirmPW;

    @FXML
    private Label email;

    @FXML
    private VBox loginBox;

    @FXML
    private Label name;

    @FXML
    private TextField newPW;

    @FXML
    private TextField old;

    @FXML
    private Label role;

    @FXML
    void onValidate(ActionEvent event) {
        changePassword();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadData();
    }

    private void loadData() {
        name.setText(user.getName());
        email.setText(user.getEmail());
        role.setText(user.getRole().getName());
        for (Login login : loginList) {
            addLoginCard(login);
        }
    }

    private void addLoginCard(Login login) {
        try{
            // Charger une carte category
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/cards/loginCard.fxml"));
            Parent loginCard = loader.load(); // Charger le fichier FXML

            LoginCard loginCardController = loader.getController(); // Récupérer le contrôleur associé
            
            loginCard.setUserData(loginCardController);

            loginCardController.setData(login);

            loginCard.setOnMouseClicked(event -> styleBinder(loginCard));

            loginBox.getChildren().add(loginCard);
        }

        catch(Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void styleBinder(Parent card) {
        // Récupérer le contrôleur associé à la carte actuelle
        LoginCard loginCard = (LoginCard) card.getUserData();
        
        // Si multipleSelector n'est pas sélectionné, itérer sur les enfants du supplierGrid
        for (Node node : loginBox.getChildren()) {
            // Vérifier si l'élément a un contrôleur (on suppose que chaque node a un contrôleur associé)
            if (node.getUserData() != null && node != card) {
                // Récupérer le contrôleur de chaque carte
                LoginCard otherCategoryCardController = (LoginCard) node.getUserData();
                // Appeler la méthode removeSelection() pour retirer le style "selected"
                otherCategoryCardController.removeSelection();
            }
        }
        loginCard.toggle();
    }

    private void changePassword() {
        // Récupérer les valeurs des champs
        String oldPassword = old.getText().trim();
        String newPassword = newPW.getText().trim();
        String comfirmPassword = confirmPW.getText().trim();
    
        // Vérifier si les champs sont vides
        if (oldPassword.isEmpty()) {
            Dialogs.showSimpleMessage("L'ancien mot de passe est obligatoire.");
            return;
        }
    
        if (newPassword.isEmpty()) {
            Dialogs.showSimpleMessage("Le nouveau mot de passe est obligatoire.");
            return;
        }


        if (newPassword.isEmpty()) {
            Dialogs.showSimpleMessage("Le mot de passe de confirmation est obligatoire.");
            return;
        }

        if(!newPassword.equals(comfirmPassword)) {
            Dialogs.showSimpleMessage("mot de passe de confirmation different");
            return;
        }


        
        // Vérifier si l'ancien et le nouveau mot de passe sont identiques
        if (oldPassword.equals(newPassword)) {
            Dialogs.showSimpleMessage("Le nouveau mot de passe doit être différent de l'ancien.");
            return;
        }
        
        // Vérifier si le nouveau mot de passe respecte les critères
        if (SessionUtil.getCurrentUser().passwordBelongToMe(newPassword)) {
            Dialogs.showSimpleMessage("L'ancien mot de passe est incorrect.");
            return;
        }
        try {
            SessionUtil.getCurrentUser().setPassword(newPassword);
            Dialogs.showSimpleMessage("mot de passe changé avec succes");
            newPW.clear();
            old.clear();
            confirmPW.clear();
        } catch (Exception e) {
            Dialogs.showSimpleMessage("une erreur s'est produite");

        }
    }

}
