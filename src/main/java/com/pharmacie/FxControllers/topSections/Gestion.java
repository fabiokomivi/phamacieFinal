package com.pharmacie.FxControllers.topSections;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.pharmacie.utilities.Updatable;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class Gestion implements Initializable{

    private Map<String, Parent> pages = new HashMap<>();

    Parent selectedSection = null;

    @FXML
    private StackPane content;

    @FXML
    private HBox onFieldBox;

    @FXML
    void onField(ActionEvent event) {

        Button clickedButton = (Button) event.getSource();

        System.out.println(clickedButton.getText().trim());
        
        onFieldChange(clickedButton);
        showPage(clickedButton.getText().trim());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Précharger les pages FXML dans le dictionnaire
        try {
            
            //pages.put("categories", FXMLLoader.load(getClass().getResource("/com/pharmacie/fxml/sections/categories.fxml")));
            //pages.put("fournisseurs", FXMLLoader.load(getClass().getResource("/com/pharmacie/fxml/sections/fournisseurs.fxml")));
            //pages.put("produits", FXMLLoader.load(getClass().getResource("/com/pharmacie/fxml/sections/produits.fxml")));
            //pages.put("stock", FXMLLoader.load(getClass().getResource("/com/pharmacie/fxml/sections/approvisionnement.fxml")));
            //pages.put("paiements", FXMLLoader.load(getClass().getResource("/com/pharmacie/fxml/sections/paiements.fxml")));
            //pages.put("roles", FXMLLoader.load(getClass().getResource("/com/pharmacie/fxml/sections/roles.fxml")));
            //pages.put("utilisateurs", FXMLLoader.load(getClass().getResource("/com/pharmacie/fxml/sections/utilisateurs.fxml")));

            pages.put("categories", loadFxml("categories"));
            pages.put("fournisseurs", loadFxml("fournisseurs"));
            pages.put("produits", loadFxml("produits"));
            pages.put("stocks", loadFxml("stocks"));
            pages.put("paiements", loadFxml("paiements"));
            //pages.put("roles", loadFxml("roles"));
            pages.put("utilisateurs", loadFxml("utilisateurs"));
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du préchargement des pages FXML");
        }

        // Charger une page par défaut
        showPage("categories");
        selectedSection = pages.get("categories");

    }

    public void onFieldChange(Button clickedButton) {
        // Réinitialiser les styles de tous les boutons dans 'onFieldBox'
        onFieldBox.getChildren().stream()
            .filter(node -> node instanceof Button)
            .map(node -> (Button) node)
            .forEach(button -> {
                button.getStyleClass().removeAll("selected");
                //button.getStyleClass().add("up"); // Réappliquer le style par défaut
            });
    
        // Appliquer le style "selected" au bouton cliqué
        clickedButton.getStyleClass().add("selected");
    }

    private void showPage(String pageKey) {
        // Vérifier si la page existe dans le dictionnaire
        Parent root = pages.get(pageKey);
        if (root!=null && !root.equals(selectedSection)) {
            // Effacer le contenu existant et afficher la nouvelle page
            content.getChildren().clear();
            
            Object object = root.getUserData();
            if (object instanceof Updatable)
                ((Updatable) root.getUserData()).update();

            content.getChildren().setAll(root);
            selectedSection = root;
        } else {
            System.out.println("Page introuvable : " + pageKey);
        }
    }

    private Parent loadFxml(String fxmlName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/sections/"+fxmlName+".fxml"));
        Parent root = loader.load();
        root.setUserData(loader.getController());
        return root;
    }
}


