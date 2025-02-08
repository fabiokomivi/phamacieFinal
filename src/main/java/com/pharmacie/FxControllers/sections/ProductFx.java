package com.pharmacie.FxControllers.sections;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.pharmacie.FxControllers.cards.MedicineCardFx;
import com.pharmacie.FxControllers.pops.CreateMedicine;
import com.pharmacie.controllers.MedicineController;
import com.pharmacie.models.Medicine;
import com.pharmacie.utilities.BehaviorSetter;
import com.pharmacie.utilities.Dialogs;
import com.pharmacie.utilities.Updatable;
import com.pharmacie.utilities.VarWrapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class ProductFx implements Initializable, Updatable {

    MedicineController medicineController = new MedicineController();
    List<Medicine> allMedicines = medicineController.getAllMedicines();

    @FXML
    private FlowPane medicinesGrid;

    @FXML
    private TextField searcher;

    @FXML
    private ToggleButton multipleSelector;

    @FXML
    void addProduct(ActionEvent event) throws IOException {
        openWindow(false);
    }

    @FXML
    void deleteProduct(ActionEvent event) throws IOException {
        deleteMedicineCard();
        allMedicines = medicineController.getAllMedicines();
    }

    @FXML
    void editProduct(ActionEvent event) throws IOException {
        openWindow(true);
        allMedicines = medicineController.getAllMedicines();
    }

    @FXML
    void onProductChoice(MouseEvent event) {

    }

    @FXML
    void onMultipleSelection(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMedicines();
        initializeSearcher();;
    }

    private void addMedicineCard(Medicine medicine) throws IOException {

        try{
            // Charger une carte medicine
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/cards/medicineCard.fxml"));
            Parent medicineCard = loader.load(); // Charger le fichier FXML

            MedicineCardFx medicineCardFxController = loader.getController(); // Récupérer le contrôleur associé
            
            medicineCard.setUserData(medicineCardFxController);

            medicineCardFxController.setData(medicine);

            medicineCard.setOnMouseClicked(event -> styleBinder(medicineCard));

            medicinesGrid.getChildren().add(medicineCard);
        }
        catch(Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void loadMedicines() {
        for(Medicine medicine : allMedicines) {
            try {
                addMedicineCard(medicine);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void openWindow(boolean edit) throws IOException {

        VarWrapper<MedicineCardFx> selectedCardWrapper = new VarWrapper(null);

        FXMLLoader windowLoader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/pops/createMedicine.fxml"));
        Parent root = windowLoader.load();

        Stage stage = BehaviorSetter.ModalDraggableTransparent(root);
    
        CreateMedicine windowController = windowLoader.getController();
    
        if (edit) {
            MedicineCardFx selectMedicineCard = null;
    
            // Rechercher la carte sélectionnée
            for (Node node : medicinesGrid.getChildren()) {
                MedicineCardFx currentMedicineCardFx = (MedicineCardFx) node.getUserData();
                if (currentMedicineCardFx != null && currentMedicineCardFx.isCardSelected()) {
                    selectMedicineCard = currentMedicineCardFx;
                    windowController.setData(currentMedicineCardFx.getMedicine());

                    selectedCardWrapper.setValue(selectMedicineCard);

                    stage.setOnHidden(e -> {
                        Medicine updatedMedicine = windowController.getMedicine();
                        if(updatedMedicine != null) {
                            MedicineCardFx unwrapedMedicineCardFx = selectedCardWrapper.getValue();
                            updateMedicineCard(unwrapedMedicineCardFx, updatedMedicine);
                        }
                    });

                    break;
                }
            }
    
            // Vérifier si une carte sélectionnée a été trouvée
            if (selectMedicineCard == null) {
                Dialogs.showAllowMessage("aucun produit selectionné");
                return;
            }

        } else {
            stage.setOnHidden(e -> {
                Medicine createdMedicine = windowController.getMedicine();
                if(createdMedicine != null) {
                    try {
                        addMedicineCard(createdMedicine);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
        stage.showAndWait();
    }

    private void updateMedicineCard(MedicineCardFx medicineCardFx, Medicine medicine) {
        medicineCardFx.setData(medicine);
    }

    private void deleteMedicineCard() throws IOException {
        // Collecter les nœuds à supprimer
        List<Node> nodesToRemove = collectSelectedNodes();
        
        if (nodesToRemove.isEmpty()) {
            Dialogs.showSimpleMessage("Aucun produit sélectionné");
            return;
        }
        
        // Afficher le message de confirmation
        if (Dialogs.showAllowMessage("Voulez-vous vraiment supprimer ces produits ?")) {
            for (Node node : nodesToRemove) {
                MedicineCardFx medicineCardFx = (MedicineCardFx) node.getUserData();
                if (medicineCardFx != null) {
                    medicineController.deleteMedicine(medicineCardFx.getMedicine().getId());
                }
            }
            medicinesGrid.getChildren().removeAll(nodesToRemove);
        }
    }

    // Méthode pour collecter les nœuds sélectionnés
    private List<Node> collectSelectedNodes() {
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : medicinesGrid.getChildren()) {
            MedicineCardFx medicineCardFx = (MedicineCardFx) node.getUserData();
            if (medicineCardFx != null && medicineCardFx.isCardSelected()) {
                nodesToRemove.add(node);
            }
        }
        return nodesToRemove;
    }

    

    private void styleBinder(Parent son) {
        // Récupérer le contrôleur associé à la carte actuelle
        MedicineCardFx medicineCardFx = (MedicineCardFx) son.getUserData();
        
        // Si multipleSelector est sélectionné, appliquez le toggle sur la carte
        if (multipleSelector.isSelected()) {
            medicineCardFx.toggle();
        } else {
            // Si multipleSelector n'est pas sélectionné, itérer sur les enfants du supplierGrid
            for (Node node : medicinesGrid.getChildren()) {
                // Vérifier si l'élément a un contrôleur (on suppose que chaque node a un contrôleur associé)
                if (node.getUserData() != null && node != son) {
                    // Récupérer le contrôleur de chaque carte
                    MedicineCardFx otherCardController = (MedicineCardFx) node.getUserData();
                    // Appeler la méthode removeSelection() pour retirer le style "selected"
                    otherCardController.removeSelection();
                }
            }
            medicineCardFx.toggle();
        }
    }

    private void initializeSearcher() {
        searcher.textProperty().addListener((observable, oldValue, newValue) -> {
            searchProducts(newValue.trim().toLowerCase());
        });
    }

    
    private void searchProducts(String query) {
        // Effacer les résultats actuels
        medicinesGrid.getChildren().clear();
    
        // Obtenir tous les médicaments
        
    
        // Parcourir les médicaments pour trouver ceux qui correspondent à la recherche
        for (Medicine medicine : allMedicines) {
            if (medicine.getName().toLowerCase().contains(query)) {
                try {
                    addMedicineCard(medicine);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void update() {
        allMedicines = medicineController.getAllMedicines();
        medicinesGrid.getChildren().clear();
        loadMedicines();
    }
}
