package com.pharmacie.FxControllers.pops;
import java.io.IOException;
import java.util.regex.Pattern;

import com.pharmacie.controllers.UserController;
import com.pharmacie.models.User;
import com.pharmacie.utilities.Dialogs;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;


public class forgot {

    private static final String EMAIL_REGEX = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    UserController userController = new UserController();

    @FXML
    private TextField forgotEmailTf;

    @FXML
    void onCancel(ActionEvent event) {
        // Obtenir la fenêtre actuelle à partir du bouton qui a déclenché l'événement
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close(); // Fermer la fenêtre
    }

    @FXML
    void onOk(ActionEvent event) throws IOException {
        User user = userController.getUserByEmail(this.forgotEmailTf.getText());

        
        if (user==null)
            Dialogs.showSimpleMessage("utilisateur n'existe pas");  
        else {
            userController.recoveryPassword(user);
            Dialogs.showSimpleMessage("mot de passe mis a jour");
        }
    }

    boolean checkedAll() {
        if (this.forgotEmailTf.getText().isEmpty() || !isValidEmail(this.forgotEmailTf.getText())) {
            makeRed(this.forgotEmailTf);
            return false;
        }
        return true;
    }

    void makeRed(TextField field) {
        // Appliquer un style rouge au TextField
        field.setStyle("-fx-border-color: red; -fx-background-color: red;");

        // Démarrer un temporisateur pour réinitialiser le style après 2 secondes
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> {
            // Réinitialiser le style après 2 secondes
            field.setStyle("-fx-border-color: green;");
        });
        pause.play();
    }

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

}
