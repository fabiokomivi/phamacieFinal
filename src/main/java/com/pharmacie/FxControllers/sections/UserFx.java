package com.pharmacie.FxControllers.sections;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.pharmacie.FxControllers.cards.UserCard;
import com.pharmacie.FxControllers.pops.CreateUser;
import com.pharmacie.controllers.UserController;
import com.pharmacie.models.User;
import com.pharmacie.utilities.BehaviorSetter;
import com.pharmacie.utilities.Dialogs;
import com.pharmacie.utilities.VarWrapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class UserFx implements Initializable{

    UserController userController = new UserController();
    List<User> allUsers = userController.getAllUsers();

    @FXML
    private ToggleButton multipleSelector;


    @FXML
    private TextField searcher;

    @FXML
    private FlowPane userGrid;

    @FXML
    void addUser(ActionEvent event) throws IOException {
        openWindow(false);
        userController.getAllUsers();
    }

    @FXML
    void editUser(ActionEvent event) throws IOException {
        openWindow(true);
        userController.getAllUsers();
    }

    @FXML
    void onMultipleSelection(ActionEvent event) {
        if (!multipleSelector.isSelected()) {
            for (Node node : userGrid.getChildren()) {
                if (node.getUserData() != null) {
                    UserCard cardController = (UserCard) node.getUserData();
                    cardController.removeSelection();
                }
            }
        }
    }

    @FXML
    void removeUser(ActionEvent event) {
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (User user : userController.getAllUsers())
            try {
                addUserCard(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        initializeSearcher();
    }


    public void openWindow(boolean edit) throws IOException {

        VarWrapper<UserCard> selectedCardWrapper = new VarWrapper(null);

        FXMLLoader windowLoader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/pops/createUtilisateur.fxml"));
        Parent root = windowLoader.load();

        Stage stage = BehaviorSetter.ModalDraggableTransparent(root);
    
        CreateUser windowController = windowLoader.getController();
    
        if (edit) {
            UserCard selectUserCard = null;
    
            // Rechercher la carte sélectionnée
            for (Node node : userGrid.getChildren()) {
                UserCard currentUserCardFx = (UserCard) node.getUserData();
                if (currentUserCardFx != null && currentUserCardFx.isCardSelected()) {
                    selectUserCard = currentUserCardFx;
                    windowController.setData(currentUserCardFx.getUser());

                    selectedCardWrapper.setValue(selectUserCard);

                    stage.setOnHidden(e -> {
                        User updatedUser = windowController.getUser();
                        if(updatedUser != null) {
                            UserCard unwrapedUserCardFx = selectedCardWrapper.getValue();
                            updateUserCard(unwrapedUserCardFx, updatedUser);
                        }
                    });

                    break;
                }
            }
    
            // Vérifier si une carte sélectionnée a été trouvée
            if (selectUserCard == null) {
                Dialogs.showAllowMessage("aucun utilisateur selectionné");
                return;
            }

        } else {
            stage.setOnHidden(e -> {
                User createdUser = windowController.getUser();
                if(createdUser != null) {
                    try {
                        addUserCard(createdUser);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
        stage.showAndWait();
    }


    private void addUserCard(User user) throws IOException {

        try{
            // Charger une carte category
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/cards/utilisateurCard.fxml"));
            Parent userCard = loader.load(); // Charger le fichier FXML

            UserCard userCardController = loader.getController(); // Récupérer le contrôleur associé
            
            userCard.setUserData(userCardController);

            userCardController.setData(user);

            userCard.setOnMouseClicked(event -> styleBinder(userCard));

            userGrid.getChildren().add(userCard);
        }
        catch(Exception e) {
            e.printStackTrace();
            return;
        }
    }

    
    private void updateUserCard(UserCard userCard, User user) {
        userCard.setData(user);
    }
    
    private void styleBinder(Parent son) {
        // Récupérer le contrôleur associé à la carte actuelle
        UserCard userCard = (UserCard) son.getUserData();
        
        // Si multipleSelector est sélectionné, appliquez le toggle sur la carte
        if (multipleSelector.isSelected()) {
            userCard.toggle();
        } else {
            // Si multipleSelector n'est pas sélectionné, itérer sur les enfants du supplierGrid
            for (Node node : userGrid.getChildren()) {
                // Vérifier si l'élément a un contrôleur (on suppose que chaque node a un contrôleur associé)
                if (node.getUserData() != null && node != son) {
                    // Récupérer le contrôleur de chaque carte
                    UserCard otherCardController = (UserCard) node.getUserData();
                    // Appeler la méthode removeSelection() pour retirer le style "selected"
                    otherCardController.removeSelection();
                }
            }
            userCard.toggle();
        }
    }


    private void initializeSearcher() {
        searcher.textProperty().addListener((observable, oldValue, newValue) -> {
            searchUsers(newValue.trim().toLowerCase());
        });
    }

    
    private void searchUsers(String query) {
        // Effacer les résultats actuels
        userGrid.getChildren().clear();
    
        for (User user : allUsers) {
            if (user.getName().toLowerCase().contains(query)) {
                try {
                    addUserCard(user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
