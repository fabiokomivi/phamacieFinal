package com.pharmacie.FxControllers.sections;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.pharmacie.controllers.SupplierController;
import com.pharmacie.models.Supplier;
import com.pharmacie.utilities.BehaviorSetter;
import com.pharmacie.utilities.Dialogs;
import com.pharmacie.utilities.VarWrapper;
import com.pharmacie.FxControllers.cards.SupplierCardFx;
import com.pharmacie.FxControllers.pops.CreateSupplier;

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



public class SupplierFx implements Initializable{

    SupplierController supplierController = new SupplierController();


    List<Supplier> allSupliers = supplierController.getAllSuppliers();
    

    @FXML
    private FlowPane supplierGrid;


    @FXML
    private TextField searcher;

    @FXML
    private ToggleButton multipleSelector;

    @FXML
    void addSupplier(ActionEvent event) throws IOException {
        openWindow(false);
        allSupliers = supplierController.getAllSuppliers();
    }

    @FXML
    void editSupplier(ActionEvent event) throws IOException {
        openWindow(true);
        allSupliers = supplierController.getAllSuppliers();
    }

    @FXML
    void removeSupplier(ActionEvent event) throws IOException {
        deleteSupplierCard();
        allSupliers = supplierController.getAllSuppliers();
    }

    @FXML
    void onClose(ActionEvent event) {
        
    }

    @FXML
    void onValidate(ActionEvent event) {
        
    }


    @FXML
    void onMultipleSelection(ActionEvent event) {
        // Si multipleSelector est désactivé, désélectionner toutes les cartes
        if (!multipleSelector.isSelected()) {
            // Parcourir tous les enfants de supplierGrid (les cartes)
            for (Node node : supplierGrid.getChildren()) {
                // Vérifier si l'élément a un contrôleur
                if (node.getUserData() != null) {
                    // Récupérer le contrôleur de chaque carte
                    SupplierCardFx cardController = (SupplierCardFx) node.getUserData();
                    // Enlever le style "selected" de chaque carte
                    cardController.removeSelection();
                }
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (Supplier supplier : allSupliers)
            try {
                addSupplierCard(supplier);
            } catch (IOException e) {
                e.printStackTrace();
            }
        initializeSearcher();;
    }


    public void openWindow(boolean edit) throws IOException {

        VarWrapper<SupplierCardFx> selectedCardWrapper = new VarWrapper(null);

        FXMLLoader windowLoader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/pops/createFournisseur.fxml"));
        Parent root = windowLoader.load();

        Stage stage = BehaviorSetter.ModalDraggableTransparent(root);
    
        CreateSupplier windowController = windowLoader.getController();
    
        if (edit) {
            SupplierCardFx selectSupplierCard = null;
    
            // Rechercher la carte sélectionnée
            for (Node node : supplierGrid.getChildren()) {
                SupplierCardFx currentSupplierCardFx = (SupplierCardFx) node.getUserData();
                if (currentSupplierCardFx != null && currentSupplierCardFx.isCardSelected()) {
                    selectSupplierCard = currentSupplierCardFx;
                    windowController.setData(currentSupplierCardFx.getSupplier());

                    selectedCardWrapper.setValue(selectSupplierCard);

                    stage.setOnHidden(e -> {
                        Supplier updatedsSupplier = windowController.getSupplier();
                        if(updatedsSupplier != null) {
                            SupplierCardFx unwrapedSupplierCardFx = selectedCardWrapper.getValue();
                            updateSupplierCard(unwrapedSupplierCardFx, updatedsSupplier);
                        }
                    });

                    break;
                }
            }
    
            // Vérifier si une carte sélectionnée a été trouvée
            if (selectSupplierCard == null) {
                Dialogs.showAllowMessage("aucun fournisseur selectionné");
                return;
            }

        } else {
            stage.setOnHidden(e -> {
                Supplier createdSupplier = windowController.getSupplier();
                if(createdSupplier != null) {
                    try {
                        addSupplierCard(createdSupplier);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
        stage.showAndWait();
    }
    
    private void addSupplierCard(Supplier supplier) throws IOException {

        try{
            // Charger une carte category
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/cards/fournisseurCard.fxml"));
            Parent supplierCard = loader.load(); // Charger le fichier FXML

            SupplierCardFx supplierCardController = loader.getController(); // Récupérer le contrôleur associé
            
            supplierCard.setUserData(supplierCardController);

            supplierCardController.setData(supplier);

            supplierCard.setOnMouseClicked(event -> styleBinder(supplierCard));

            supplierGrid.getChildren().add(supplierCard);
        }
        catch(Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void updateSupplierCard(SupplierCardFx supplierCardFx, Supplier supplier) {
        supplierCardFx.setData(supplier);
    }
    
    private void deleteSupplierCard() throws IOException {
        // Collecter les nœuds à supprimer
        List<Node> nodesToRemove = collectSelectedNodes();
        
        if (nodesToRemove.isEmpty()) {
            Dialogs.showSimpleMessage("Aucun fournisseur sélectionné");
            return;
        }
        
        // Afficher le message de confirmation
        if (Dialogs.showAllowMessage("Voulez-vous vraiment supprimer ces fournisseurs ?")) {
            for (Node node : nodesToRemove) {
                SupplierCardFx supplierCardFx = (SupplierCardFx) node.getUserData();
                if (supplierCardFx != null) {
                    supplierController.deleteSupplier(supplierCardFx.getSupplier().getId());
                }
            }
            supplierGrid.getChildren().removeAll(nodesToRemove);
        }
    }

    // Méthode pour collecter les nœuds sélectionnés
    private List<Node> collectSelectedNodes() {
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : supplierGrid.getChildren()) {
            SupplierCardFx supplierCardFx = (SupplierCardFx) node.getUserData();
            if (supplierCardFx != null && supplierCardFx.isCardSelected()) {
                nodesToRemove.add(node);
            }
        }
        return nodesToRemove;
    }

    

    private void styleBinder(Parent son) {
        // Récupérer le contrôleur associé à la carte actuelle
        SupplierCardFx supplierCardFx = (SupplierCardFx) son.getUserData();
        
        // Si multipleSelector est sélectionné, appliquez le toggle sur la carte
        if (multipleSelector.isSelected()) {
            supplierCardFx.toggle();
        } else {
            // Si multipleSelector n'est pas sélectionné, itérer sur les enfants du supplierGrid
            for (Node node : supplierGrid.getChildren()) {
                // Vérifier si l'élément a un contrôleur (on suppose que chaque node a un contrôleur associé)
                if (node.getUserData() != null && node != son) {
                    // Récupérer le contrôleur de chaque carte
                    SupplierCardFx otherCardController = (SupplierCardFx) node.getUserData();
                    // Appeler la méthode removeSelection() pour retirer le style "selected"
                    otherCardController.removeSelection();
                }
            }
            supplierCardFx.toggle();
        }
    }

    public void update() {
        
    }

    private void initializeSearcher() {
        searcher.textProperty().addListener((observable, oldValue, newValue) -> {
            searchSuppliers(newValue.trim().toLowerCase());
        });
    }

    
    private void searchSuppliers(String query) {
        // Effacer les résultats actuels
        supplierGrid.getChildren().clear();
    
        // Obtenir tous les médicaments
        
    
        // Parcourir les médicaments pour trouver ceux qui correspondent à la recherche
        for (Supplier supplier : allSupliers) {
            if (supplier.getName().toLowerCase().contains(query)) {
                try {
                    addSupplierCard(supplier);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
