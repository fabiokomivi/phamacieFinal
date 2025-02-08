package com.pharmacie.FxControllers.topSections;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import com.pharmacie.FxControllers.cards.PurchaseCard;
import com.pharmacie.FxControllers.pops.CreatePurchase;
import com.pharmacie.controllers.MedicineController;
import com.pharmacie.controllers.MedicinePurchaseController;
import com.pharmacie.controllers.PurchaseController;
import com.pharmacie.controllers.StockController;
import com.pharmacie.models.Medicine;
import com.pharmacie.models.MedicinePurchase;
import com.pharmacie.models.PaymentMode;
import com.pharmacie.models.Purchase;
import com.pharmacie.models.Stock;
import com.pharmacie.session.SessionUtil;
import com.pharmacie.utilities.BehaviorSetter;
import com.pharmacie.utilities.Dialogs;
import com.pharmacie.utilities.InvoiceGenerator;
import com.pharmacie.utilities.VarWrapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class Ventes implements Initializable{

    PurchaseController purchaseController = new PurchaseController();
    StockController stockController = new StockController();
    MedicinePurchaseController medicinePurchaseController = new MedicinePurchaseController();
    MedicineController medicineController = new MedicineController();
    InvoiceGenerator invoiceGenerator = new InvoiceGenerator();

    List<Purchase> allPurchases  = purchaseController.getAllPurchases();

    @FXML
    private Button addPurchaseBtn;

    @FXML
    private Button deletePurchaseBtn;

    @FXML
    private Button detailsBtn;

    @FXML
    private Button editPurchaseBtn;

    @FXML
    private Button invoiceBtn;

    @FXML
    private TextField researcher;

    @FXML
    private Button validatePurchaseBtn;

    @FXML
    private VBox purchasesBox;

    @FXML
    void addPurchase(ActionEvent event) throws IOException {
        openWindow(false);
    }

    @FXML
    void deletePurchase(ActionEvent event) {
        try {
            // Trouver le Node correspondant à la carte sélectionnée
            Node selectedNode = purchasesBox.getChildren().stream()
                .filter(node -> {
                    PurchaseCard card = (PurchaseCard) node.getUserData();
                    return card != null && card.isCardSelected();
                })
                .findFirst()
                .orElse(null);

            if (selectedNode == null) {
                Dialogs.showSimpleMessage("Veuillez sélectionner une vente.");
                return;
            }

            // Récupérer la carte de vente et l'objet Purchase
            PurchaseCard selectedPurchaseCard = (PurchaseCard) selectedNode.getUserData();
            Purchase selectedPurchase = selectedPurchaseCard.getPurchase();

            // Vérifier si la vente est validée
            if (selectedPurchase.getStatus()) {
                Dialogs.showSimpleMessage("Cette vente ne peut pas être supprimée car elle a déjà été validée.");
                return;
            }

            purchaseController.deletePurchase(selectedPurchase.getId());
            PaymentMode paymentMode = selectedPurchase.getPaymentMode();
            paymentMode.removePurchase(selectedPurchase);

            // Supprimer le Node de l'interface
            purchasesBox.getChildren().remove(selectedNode);

            // Message de succès
            Dialogs.showSimpleMessage("Vente supprimée avec succès.");
        } catch (Exception e) {
            // Gestion des erreurs
            Dialogs.showSimpleMessage("Une erreur est survenue lors de la suppression de la vente : " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    void editPurchase(ActionEvent event) throws IOException {
        openWindow(true);
    }

    @FXML
    void onDetails(ActionEvent event) {

    }

    @FXML
    void onInvoice(ActionEvent event) throws Exception {
        // Trouver la vente sélectionnée
        PurchaseCard selectedPurchaseCard = null;
        for (Node node : purchasesBox.getChildren()) {
            PurchaseCard currentController = (PurchaseCard) node.getUserData();
            if (currentController.isCardSelected()) {
                selectedPurchaseCard = currentController;
                break;
            }
        }

        if (selectedPurchaseCard == null) {
            Dialogs.showSimpleMessage("Veuillez sélectionner une vente.");
            return;
        }

        // Récupérer l'achat sélectionné
        Purchase selectedPurchase = selectedPurchaseCard.getPurchase();

        if (selectedPurchase.getStatus()){
            invoiceGenerator.generateInvoice(selectedPurchase);
        }
        else {
            Dialogs.showSimpleMessage("veuillez d'abord valider l'achat");
        }
    }

    @FXML
    void onPurshaseDate(ActionEvent event) {

    }

    @FXML
    void onResearch(ActionEvent event) {

    }

    @FXML
    void onStatusBox(ActionEvent event) {

    }

    @FXML
    void validatePurchase(ActionEvent event) {
        // Trouver la vente sélectionnée
        PurchaseCard selectedPurchaseCard = null;
        for (Node node : purchasesBox.getChildren()) {
            PurchaseCard currentController = (PurchaseCard) node.getUserData();
            if (currentController.isCardSelected()) {
                selectedPurchaseCard = currentController;
                break;
            }
        }

        if (selectedPurchaseCard == null) {
            Dialogs.showSimpleMessage("Veuillez sélectionner une vente.");
            return;
        }

        // Récupérer l'achat sélectionné
        Purchase selectedPurchase = selectedPurchaseCard.getPurchase();

        if (!selectedPurchase.getStatus()){

            // Vérification si l'achat contient des médicaments
            if (selectedPurchase.getMedicinesLink().isEmpty()) {
                Dialogs.showSimpleMessage("La vente sélectionnée ne contient aucun médicament.");
                return;
            }
    
            // Parcourir les médicaments de l'achat
            for (MedicinePurchase medicinePurchase : selectedPurchase.getMedicinesLink()) {
                Medicine medicine = medicinePurchase.getMedicine();
                int quantityNeeded = medicinePurchase.getQuantity();
    
                // Récupérer les stocks triés par date d'expiration (et par date de fabrication si nécessaire)
                Set<Stock> sortedStocks = medicine.getStocks(); // Stocks triés par @OrderBy("manufactureDate ASC, acquisitionDate ASC")
                
                // Essayer de satisfaire la commande en retirant les stocks les plus vieux en priorité
                for (Stock stock : sortedStocks) {
                    if (quantityNeeded <= 0) break; // Besoin déjà satisfait
    
                    if (stock.getQuantity() > 0) {
                        if (stock.getQuantity() >= quantityNeeded) {
                            // Si la quantité du stock actuel est suffisante
                            stock.setQuantity(stock.getQuantity() - quantityNeeded);
                            quantityNeeded = 0; // Besoin satisfait
                            stockController.updateStock(stock); // Mettre à jour le stock
                        } else {
                            // Si la quantité du stock actuel est insuffisante
                            quantityNeeded -= stock.getQuantity();
                            stock.setQuantity(0); // Stock épuisé
                            stockController.updateStock(stock); // Mettre à jour le stock
                        }
                    }
                }
    
                if (quantityNeeded > 0) {
                    // Si tous les stocks combinés ne couvrent pas la quantité nécessaire
                    Dialogs.showSimpleMessage(
                        "Stock insuffisant pour le médicament : " + medicine.getName());
                    return; // Interrompre la validation de la vente
                }
            }
    
            // Mettre à jour l'état de la vente (par exemple, marquer comme validée)
            selectedPurchase.setStatus(true); // Vous pouvez aussi utiliser un statut "VALIDATED"
            purchaseController.updatePurchase(selectedPurchase);
    
            // Recharger les données pour éviter les problèmes de cache ou incohérences
            selectedPurchase = purchaseController.getPurchaseById(selectedPurchase.getId());
    
            // Mettre à jour la carte de la vente
            updatePurchaseCard(selectedPurchaseCard, selectedPurchase);
    
            // Message de succès
            SessionUtil.updateCurrentUser();
            Dialogs.showSimpleMessage("Vente validée avec succès.");
        }
        else {
            Dialogs.showSimpleMessage("Vente deja validée.");
        }

    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPurchases();
        initializeSearcher();;
    }

    public void openWindow(boolean edit) throws IOException {

        VarWrapper<PurchaseCard> selectedCardWrapper = new VarWrapper(null);

        FXMLLoader windowLoader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/pops/createPurchase.fxml"));
        Parent root = windowLoader.load();

        Stage stage = BehaviorSetter.ModalDraggableTransparent(root);
    
        CreatePurchase windowController = windowLoader.getController();
    
        if (edit) {
            PurchaseCard selectPurchaseCard = null;
    
            // Rechercher la carte sélectionnée
            for (Node node : purchasesBox.getChildren()) {
                PurchaseCard currentPurchaseCard = (PurchaseCard) node.getUserData();
                if (currentPurchaseCard != null && currentPurchaseCard.isCardSelected()) {
                    selectPurchaseCard = currentPurchaseCard;
                    windowController.setData(currentPurchaseCard.getPurchase());

                    selectedCardWrapper.setValue(selectPurchaseCard);

                    stage.setOnHidden(e -> {
                        Purchase updatedPurchase = windowController.getPurchase();
                        if(updatedPurchase != null) {
                            PurchaseCard unwrapedPurchaseCard = selectedCardWrapper.getValue();
                            updatedPurchase.calculateTotal();
                            allPurchases.add(updatedPurchase);
                            updatePurchaseCard(unwrapedPurchaseCard, updatedPurchase);
                            allPurchases  = purchaseController.getAllPurchases();
                        }
                    });

                    break;
                }
            }
    
            // Vérifier si une carte sélectionnée a été trouvée
            if (selectPurchaseCard == null) {
                Dialogs.showAllowMessage("aucun achat selectionné");
                return;
            } else if(selectPurchaseCard.getPurchase().getStatus()) {
                Dialogs.showSimpleMessage("cette vente est deja validée");
                return;
            }

        } else {
            stage.setOnHidden(e -> {
                Purchase createdPurchase = windowController.getPurchase();
                if(createdPurchase != null) {
                    try {
                        addPurchaseCard(createdPurchase);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
        stage.showAndWait();
    }


    private void addPurchaseCard(Purchase purchase) throws IOException {

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/cards/purchaseCard.fxml"));
            Parent purchaseCard = loader.load(); // Charger le fichier FXML

            PurchaseCard purchaseCardFxController = loader.getController(); // Récupérer le contrôleur associé
            
            purchaseCard.setUserData(purchaseCardFxController);

            purchaseCardFxController.setData(purchase);

            purchaseCard.setOnMouseClicked(event -> styleBinder(purchaseCard));

            purchasesBox.getChildren().add(purchaseCard);
        }
        catch(Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private void loadPurchases() {
        for(Purchase purchase : allPurchases) {
            try {
                addPurchaseCard(purchase);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void styleBinder(Parent son) {
        PurchaseCard purchaseCard = (PurchaseCard) son.getUserData();
        for (Node node : purchasesBox.getChildren()) {
            // Vérifier si l'élément a un contrôleur (on suppose que chaque node a un contrôleur associé)
            if (node.getUserData() != null && node != son) {
                // Récupérer le contrôleur de chaque carte
                PurchaseCard otherCardController = (PurchaseCard) node.getUserData();
                // Appeler la méthode removeSelection() pour retirer le style "selected"
                otherCardController.removeSelection();
            }
        }
        purchaseCard.toggle();
        
    }    

    private void updatePurchaseCard(PurchaseCard purchaseCard, Purchase purchase) {
        purchaseCard.setData(purchase);
    }

    private void initializeSearcher() {
        researcher.textProperty().addListener((observable, oldValue, newValue) -> {
            searchMedicines(newValue.trim().toLowerCase());
        });
    }

    
    private void searchMedicines(String query) {
        // Si la requête est null ou vide, charger tous les achats
        if (query == null || query.trim().isEmpty()) {
            loadPurchases();
            return;
        }
    
        // Effacer les résultats actuels
        purchasesBox.getChildren().clear();
    
        // Variable pour vérifier si un résultat a été trouvé
        boolean foundMatch = false;
    
        // Parcourir les achats pour trouver ceux qui correspondent à la recherche
        for (Purchase purchase : allPurchases) {
            // Vérification si le client est non null et si le nom correspond à la requête
            if (purchase.getClient() != null && purchase.getClient().getName().toLowerCase().contains(query.toLowerCase())) {
                try {
                    addPurchaseCard(purchase);
                    foundMatch = true; // Indiquer qu'un résultat a été trouvé
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    
        // Si aucun résultat n'a été trouvé, charger tous les achats
        if (!foundMatch) {
            loadPurchases();
        }
    }
    
}
