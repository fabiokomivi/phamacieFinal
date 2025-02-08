package com.pharmacie.FxControllers.sections;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.pharmacie.FxControllers.cards.MedicineStockCard;
import com.pharmacie.FxControllers.cards.StockCard;
import com.pharmacie.FxControllers.pops.CreateStock;
import com.pharmacie.controllers.MedicineController;
import com.pharmacie.controllers.StockController;
import com.pharmacie.models.Medicine;
import com.pharmacie.models.Stock;
import com.pharmacie.utilities.BehaviorSetter;
import com.pharmacie.utilities.Dialogs;
import com.pharmacie.utilities.OneSelectable;
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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StockFx implements Initializable, Updatable, OneSelectable {

    MedicineController medicineController = new MedicineController();
    StockController stockController = new StockController();
    List<Medicine> allMedicines = medicineController.getAllMedicines();

    @FXML
    private ToggleButton multipleSelector;

    @FXML
    private TextField searcher;

    @FXML
    private VBox medicinesBox;

    @FXML
    private VBox stockBox;


    @FXML
    void addStock(ActionEvent event) throws IOException {
        openWindow(false);
        allMedicines = medicineController.getAllMedicines();
    }

    @FXML
    void onMultipleSelection(ActionEvent event) {

    }

    @FXML
    void removeStock(ActionEvent event) {
        deleteStock();
        allMedicines = medicineController.getAllMedicines();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadMedicines();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initializeSearcher();;
    }

    private void loadMedicines() throws IOException {
        allMedicines = medicineController.getAllMedicines();
        for(Medicine medicine : allMedicines) {
            addMedicineStockCard(medicine);
        }
    }

    private void loadStocks(Medicine medicine) {
        stockBox.getChildren().clear();
        for (Stock stock : medicine.getStocks()){
            try {
                addStockCard(stock);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void addMedicineStockCard(Medicine medicine) throws IOException {
        
        FXMLLoader medicineStockCardLoader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/cards/medicineStockCard.fxml"));
        Parent medicineStockCard =  medicineStockCardLoader.load();

        MedicineStockCard medicineStockCardController = medicineStockCardLoader.getController();
        medicineStockCardController.setData(medicine);
        medicineStockCard.setUserData(medicineStockCardController);
        medicineStockCardController.setListener(this);

        medicinesBox.getChildren().add(medicineStockCard);
    }

    private void addStockCard(Stock stock) throws IOException {
        
        FXMLLoader stockCardLoader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/cards/stockCard.fxml"));
        Parent stockCard =  stockCardLoader.load();

        StockCard stockCardController = stockCardLoader.getController();
        stockCardController.setData(stock);
        stockCard.setUserData(stockCardController);
        stockCard.setOnMouseClicked(e -> styleBinder(stockCard));

        stockBox.getChildren().add(stockCard);
    }

    @Override
    public void update() {
        medicinesBox.getChildren().clear();
        try {
            loadMedicines();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void styleBinder(Parent son) {
        // Récupérer le contrôleur associé à la carte actuelle
        StockCard stockCard = (StockCard) son.getUserData();
        
        // Si multipleSelector est sélectionné, appliquez le toggle sur la carte
        if (multipleSelector.isSelected()) {
                stockCard.toggle();
        } else {
            for (Node node : stockBox.getChildren()) {
                if (node.getUserData() != null && node != son) {
                    // Récupérer le contrôleur de chaque carte
                    StockCard otherCardController = (StockCard) node.getUserData();
                    // Appeler la méthode removeSelection() pour retirer le style "selected"
                    otherCardController.removeSelection();
                }
            }
            stockCard.toggle();
        }
    }

    public void openWindow(boolean edit) throws IOException {

        VarWrapper<MedicineStockCard> selectedMedicineStockCardWrapper = new VarWrapper(null);
        VarWrapper<StockCard> selectedStockCardWrapper = new VarWrapper(null);

        MedicineStockCard selectMedicineStockCard = null;
        StockCard  selectedSCard = null;

        for (Node node : medicinesBox.getChildren()){
            MedicineStockCard currentMedicineStockCard = (MedicineStockCard) node.getUserData();
            if (currentMedicineStockCard.isCardSelected()) {
                selectMedicineStockCard = currentMedicineStockCard;
                selectedMedicineStockCardWrapper.setValue(currentMedicineStockCard);
                break;
            }
        }
        if(((MedicineStockCard) selectedMedicineStockCardWrapper.getValue())==null) {
            Dialogs.showSimpleMessage("veuillez choisir un produit");
            return;
        }

        FXMLLoader windowLoader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/pops/createStock.fxml"));
        Parent root = windowLoader.load();

        Stage stage = BehaviorSetter.ModalDraggableTransparent(root);
    
        CreateStock windowController = windowLoader.getController();
    
        if (edit) {
            
        } else {
            windowController.setData(((MedicineStockCard) selectedMedicineStockCardWrapper.getValue()).getMedicine());
            stage.setOnHidden(e -> {
                Stock createdsStock = windowController.getStock();
                if(createdsStock != null) {
                    try {
                        addStockCard(createdsStock);
                        ((MedicineStockCard) selectedMedicineStockCardWrapper.getValue()).update();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
        stage.showAndWait();
    }

    @Override
    public void onSelection(Object selectedCard) {
        for (Node node : medicinesBox.getChildren()) {
            MedicineStockCard medicineStockCard = (MedicineStockCard) node.getUserData();
            if (medicineStockCard != selectedCard) {
                medicineStockCard.removeSelection();
            }
        }

        if(((MedicineStockCard)selectedCard).isCardSelected()) {
            Medicine medicine = ((MedicineStockCard) selectedCard).getMedicine();
            loadStocks(medicine);
        }
        else {
            clearStockBox();
            return;
        }
        
    }

    public void clearStockBox() {
        stockBox.getChildren().clear();
    }

    private void deleteStock() {
        // Initialiser un wrapper pour la carte de médicament sélectionnée
        VarWrapper<MedicineStockCard> selectedMedicineStockWrapper = new VarWrapper<>(null);
    
        // Trouver la carte de médicament sélectionnée
        for (Node node : medicinesBox.getChildren()) {
            MedicineStockCard medicineStockController = (MedicineStockCard) node.getUserData();
            if (medicineStockController != null && medicineStockController.isCardSelected()) {
                selectedMedicineStockWrapper.setValue(medicineStockController);
                break;
            }
        }
    
        // Vérifier si une carte a été sélectionnée
        if (selectedMedicineStockWrapper.getValue() == null) {
            Dialogs.showSimpleMessage("Veuillez sélectionner un produit.");
            return;
        }
    
        // Récupérer les nœuds sélectionnés pour suppression
        List<Node> nodesToRemove = collectSelectedNodes();
        if (nodesToRemove.isEmpty()) {
            Dialogs.showSimpleMessage("Veuillez sélectionner au moins un stock.");
            return;
        }
    
        // Supprimer les stocks sélectionnés
        for (Node node : nodesToRemove) {
            StockCard controller = (StockCard) node.getUserData();
            if (controller != null) {
                Medicine medicine = controller.getStock().getMedicine();
                Stock stock = controller.getStock();
                if (medicine != null && stock != null) {
                    medicine.getStocks().remove(stock);
                    stockController.deleteStock(stock.getId());
                }
            }
        }
    
        // Supprimer les nœuds de l'interface utilisateur
        stockBox.getChildren().removeAll(nodesToRemove);
    
        // Mettre à jour la carte du médicament
        selectedMedicineStockWrapper.getValue().update();
    }

    private List<Node> collectSelectedNodes() {
        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : stockBox.getChildren()) {
            StockCard stockCard = (StockCard) node.getUserData();
            if (stockCard != null && stockCard.isCardSelected()) {
                nodesToRemove.add(node);
            }
        }
        return nodesToRemove;
    }

    private void initializeSearcher() {
        searcher.textProperty().addListener((observable, oldValue, newValue) -> {
            searchProducts(newValue.trim().toLowerCase());
            clearStockBox();
        });
    }

    
    private void searchProducts(String query) {
        // Effacer les résultats actuels
        medicinesBox.getChildren().clear();
    
        // Obtenir tous les médicaments
        
    
        // Parcourir les médicaments pour trouver ceux qui correspondent à la recherche
        for (Medicine medicine : allMedicines) {
            if (medicine.getName().toLowerCase().contains(query)) {
                try {
                    addMedicineStockCard(medicine);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
