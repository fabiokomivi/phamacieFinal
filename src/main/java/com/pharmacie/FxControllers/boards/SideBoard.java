package com.pharmacie.FxControllers.boards;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.pharmacie.session.SessionUtil;
import com.pharmacie.utilities.Updatable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class SideBoard implements Initializable{

    private Map<String, Parent> pages = new HashMap<>();
    
    @FXML
    private VBox adminControl;

    @FXML
    private AnchorPane sideboard;

    @FXML
    private StackPane content;

    @FXML
    void onNavButtonSelected(ActionEvent event) {

        Button clickedButton = (Button) event.getSource();

        System.out.println(clickedButton.getText().trim());

        if(clickedButton.getText().trim().equals("quitter")) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
        
        onNavChange(clickedButton);
        showPage(clickedButton.getText().trim());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(!SessionUtil.getCurrentUser().isRoot())
            adminControl.setVisible(false);
            
        // Précharger les pages FXML dans le dictionnaire
        FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/topSections/dashboard.fxml"));
        Parent dashboard = new Parent() {
            
        };

        FXMLLoader statistiqueLoader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/topSections/statistiques.fxml"));
        Parent statistique = new Parent() {
            
        };

        try {
            dashboard = dashboardLoader.load();
            statistique = statistiqueLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dashboard.setUserData(dashboardLoader.getController());
        statistique.setUserData(statistiqueLoader.getController());

        try {
            pages.put("dashboard", dashboard);
            pages.put("statistiques", statistique);
            pages.put("ventes", FXMLLoader.load(getClass().getResource("/com/pharmacie/fxml/topSections/ventes.fxml")));
            pages.put("gestion", FXMLLoader.load(getClass().getResource("/com/pharmacie/fxml/topSections/gestion.fxml")));
            pages.put("parametres", FXMLLoader.load(getClass().getResource("/com/pharmacie/fxml/topSections/parametres.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du préchargement des pages FXML");
        }

        // Charger une page par défaut
        showPage("dashboard");

    }



    public void onNavChange(Button clickedButton) {

        // Parcourir toute la hiérarchie de VBox dans 'sideboard' pour trouver les boutons
        sideboard.getChildren().stream()
            .filter(node -> node instanceof VBox) // Trouver les VBox de niveau 1
            .flatMap(vbox -> ((VBox) vbox).getChildren().stream()) // Parcourir leurs enfants
            .filter(node -> node instanceof Button) // Trouver les boutons
            .forEach(node -> {
                Button button = (Button) node;
                // Supprimer les styles 'up' et 'down'
                button.getStyleClass().removeAll("selected");
            });

        // Ajouter le style 'down' au bouton cliqué
        ///clickedButton.getStyleClass().remove("up");
        clickedButton.getStyleClass().add("selected");
    }

    private void showPage(String pageKey) {
        // Vérifier si la page existe dans le dictionnaire
        Parent root = pages.get(pageKey);
        if (root!=null) {
            // Effacer le contenu existant et afficher la nouvelle page
            content.getChildren().clear();

            if(root.getUserData() instanceof Updatable) {
                ((Updatable) root.getUserData()).update();
            }

            content.getChildren().setAll(root);
        } else {
            System.out.println("Page introuvable : " + pageKey);
        }
    }

    

}



