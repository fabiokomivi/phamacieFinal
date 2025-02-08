package com.pharmacie.FxControllers.authentify;

import java.io.IOException;
import java.util.regex.Pattern;


import com.pharmacie.controllers.UserController;
import com.pharmacie.controllers.RoleController;
import com.pharmacie.controllers.UserRoleController;
import com.pharmacie.models.User;
import com.pharmacie.models.Role;
import com.pharmacie.utilities.Dialogs;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SignUp {

    private static final String EMAIL_REGEX = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    UserController userController = new UserController();
    RoleController roleController = new RoleController();
    UserRoleController userRoleController = new UserRoleController();

    @FXML
    private PasswordField confirmPasswordPF;

    @FXML
    private TextField emailPF;

    @FXML
    private TextField namePF;
    
    @FXML
    private PasswordField passwordPF;

    @FXML
    private TextField prenamePF;

     @FXML
     private Button togglePasswordButton;
     
     
    @FXML
    void onSignUp(ActionEvent event) throws IOException {

        if (!checkedAll())
            return;

        final String name = this.namePF.getText();
        final String prename = this.prenamePF.getText();
        final String email = this.emailPF.getText();
        final String password = this.passwordPF.getText();

        final String completeName = name + " "  + prename;
            
        // Obtenir le contrôleur associé
        try {
            Role role = roleController.getRoleById(1);

            User user = new User(completeName, password, null, email, true);
            
            user.setRole(role);
            userController.saveUser(user);            

            Dialogs.showSimpleMessage("inscription reussie");

            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/authentify/login.fxml"));
            Parent root = loader1.load();

            Scene scene = new Scene(root, 1200, 600);
            Stage stage = new Stage();

            stage.setScene(scene);
            stage.setTitle("");

            Stage signupStage = (Stage) (((Node) event.getSource()).getScene().getWindow());
            signupStage.setOnHidden(e -> stage.show()
            );

            signupStage.close();
            
        } catch (Exception e) {
            System.out.println(e);
            Dialogs.showSimpleMessage("inscription echouée");
        }
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

    boolean checkedAll () {
        if (this.namePF.getText().isEmpty()) {
            makeRed(this.namePF);
            return false;
        }
        else if (this.prenamePF.getText().isEmpty()) {
            makeRed(this.prenamePF);
            return false;
        }
        else if (this.emailPF.getText().isEmpty() || !isValidEmail(this.emailPF.getText())) {
            makeRed(this.emailPF);
            return false;
        }
        else if (this.passwordPF.getText().isEmpty()) {
            makeRed(this.passwordPF);
            return false;
        }
        else if (this.confirmPasswordPF.getText().isEmpty()) {
            makeRed(this.confirmPasswordPF);
            return false;
        }
        else if (!this.passwordPF.getText().equals(this.confirmPasswordPF.getText())) {
            makeRed(this.passwordPF);
            makeRed(this.confirmPasswordPF);
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }



}
