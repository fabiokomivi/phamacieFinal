package com.pharmacie.FxControllers.pops;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import com.pharmacie.controllers.RoleController;
import com.pharmacie.controllers.UserController;
import com.pharmacie.models.User;
import com.pharmacie.session.SessionUtil;
import com.pharmacie.utilities.Dialogs;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CreateUser implements Initializable{

    User user = null;

    private static final String EMAIL_REGEX = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    UserController userController = new UserController();
    RoleController roleController = new RoleController();

    @FXML
    private CheckBox adminCheck;

    @FXML
    private HBox adminRole;

    @FXML
    private VBox rootBox;

    @FXML
    private TextField contact;

    @FXML
    private TextField email;

    @FXML
    private CheckBox employeCheck;

    @FXML
    private HBox eployeRole;

    @FXML
    private TextField fullName;

    @FXML
    private Label title;

    @FXML
    void onClose(ActionEvent event) {
        closeWindow(event);
    }

    @FXML
    void onValidate(ActionEvent event) {
        if (!checkedAll())
            return;

        final String name = fullName.getText();
        final String userEmail = email.getText();
        final String userContact = contact.getText();
        final String password = "employe";

        if (user == null) {
            user = new User(name, password, userContact, userEmail, true);
            if(SessionUtil.getCurrentUser().isRoot()){
                if(adminCheck.isSelected())
                user.setRole(roleController.getRoleById(2));
                else
                user.setRole(roleController.getRoleById(3));
                userController.updateUser(user);
            }
        } else {
            user.setName(name);
            user.setEmail(userEmail);
            user.setContact(userContact);
            if(SessionUtil.getCurrentUser().isRoot()){
                if(adminCheck.isSelected())
                user.setRole(roleController.getRoleById(2));
                else
                user.setRole(roleController.getRoleById(3));
                userController.saveUser(user);
            }         
        }
        closeWindow(event);
    }

    public void setData(User user) {
        this.user= user;

        fullName.setText(user.getName());
        contact.setText(user.getContact());
        email.setText(user.getEmail());     
    }

    public User getUser() {
        return user;
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(SessionUtil.getCurrentUser().isRoot()) {
            rootBox.setVisible(true);
            System.out.println("admin");
        }
        else {
            rootBox.setVisible(false);
            System.out.println("non admin");
        }
        SessionUtil.getCurrentUser().toString();
    }


    boolean checkedAll() {
        if (this.fullName.getText().isEmpty()) {
            makeRed(this.email);
            Dialogs.showSimpleMessage("veuillez remplir le nom");
            return false;
        }
        else if (this.email.getText().isEmpty() || !isValidEmail(this.email.getText())) {
            Dialogs.showSimpleMessage("email invalide");
            makeRed(this.email);
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
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
}
