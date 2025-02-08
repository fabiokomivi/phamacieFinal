package com.pharmacie.FxControllers.pops;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import com.pharmacie.FxControllers.cards.MedicinePurchaseCard;
import com.pharmacie.FxControllers.cards.MedicinePurchasedCard;
import com.pharmacie.controllers.MedicineController;
import com.pharmacie.controllers.MedicinePurchaseController;
import com.pharmacie.controllers.PaymentModeController;
import com.pharmacie.controllers.PurchaseController;
import com.pharmacie.models.Client;
import com.pharmacie.models.Medicine;
import com.pharmacie.models.MedicinePurchase;
import com.pharmacie.models.PaymentMode;
import com.pharmacie.models.Purchase;
import com.pharmacie.session.SessionUtil;
import com.pharmacie.utilities.BehaviorSetter;
import com.pharmacie.utilities.Dialogs;
import com.pharmacie.utilities.ImageHelper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class CreatePurchase implements Initializable{

    MedicineController medicineController = new MedicineController();
    PurchaseController purchaseController = new PurchaseController();
    MedicinePurchaseController medicinePurchaseController = new MedicinePurchaseController();
    PaymentModeController paymentModeController = new PaymentModeController();
    Purchase purchase = null;
    Medicine medicineSelected = null;
    Client selectionedClient = null;
    ImageHelper imageHelper = new ImageHelper();

    List<Medicine> allMedicines = medicineController.getAllMedicines();

    @FXML
    private VBox chosenMedicinesBox;

    @FXML
    private TextField clientContactTF;

    @FXML
    private TextField clientEmailTF;

    @FXML
    private TextField clientNameTF;

    @FXML
    private ImageView medicineIV;

    @FXML
    private Label medicineNameLabel;

    @FXML
    private TextField medicineQuantityTF;

    @FXML
    private VBox medicinesBox;

    @FXML
    private Button onValidate;

    @FXML
    private TextField searcher;

    @FXML
    private ComboBox<PaymentMode> paymentBox;


    @FXML
    void onCancel(ActionEvent event) {

    }

    @FXML
    void onDelete(ActionEvent event) {
        // Vérifiez qu'une ligne est sélectionnée
        Node selectedNode = null;

        for (Node node : chosenMedicinesBox.getChildren()) {
            if (((MedicinePurchasedCard) node.getUserData()).isCardSelected()) {
                selectedNode = node;
                break;
            }
        }

        if (selectedNode == null) {
            Dialogs.showSimpleMessage("Veuillez sélectionner un médicament à supprimer.");
            return;
        }

        // Récupérer le MedicinePurchasedCard correspondant
        MedicinePurchasedCard selectedCard = (MedicinePurchasedCard) selectedNode.getUserData();
        Medicine selectedMedicine = selectedCard.getMedicine();

        // Supprimer les liens avec la commande existante
        if (purchase != null) {
            MedicinePurchase medicinePurchase = purchase.getMedicinePurchaseFor(selectedMedicine);

            if (medicinePurchase != null) {
                // Casser le lien avec l'entité Purchase
                purchase.removeMedicinePurchase(medicinePurchase);
                // Supprimer de la base de données
                medicinePurchaseController.deleteMedicinePurchase(medicinePurchase.getId());
            }
        }

        // Supprimer le nœud de l'interface utilisateur
        chosenMedicinesBox.getChildren().remove(selectedNode);

        // Recalculer le total si nécessaire
        if (purchase != null) {
            purchase.calculateTotal();
        }
        Dialogs.showSimpleMessage("Le médicament a été supprimé avec succès.");
    }

    @FXML
    void onChooseClient(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/pops/clientChoose.fxml"));
        Parent root = loader.load();
        ClientChoose controller = loader.getController();
        Stage stage = BehaviorSetter.ModalDraggableTransparent(root);

        stage.setOnHidden(e -> {
            this.selectionedClient = controller.getClient();

            if(selectionedClient != null) {
                if(selectionedClient.getName()!=null)
                    clientNameTF.setText(selectionedClient.getName());
    
                if(selectionedClient.getContact()!=null)
                    clientContactTF.setText(selectionedClient.getContact());
    
                if(selectionedClient.getEmail()!=null)
                    clientEmailTF.setText(selectionedClient.getEmail());
            }
        });
        stage.showAndWait();
    }

    @FXML
    void onChooseQuantity(ActionEvent event) throws NumberFormatException, IOException {
        if(checkStock()) {
            addMedicinePurchasedCard(medicineSelected, Integer.parseInt(medicineQuantityTF.getText().trim()));
        }
    }

    @FXML
    void onValidate(ActionEvent event) {
        validatePurchase();
        SessionUtil.updateCurrentUser();
        closeWindow(event); // Fermer la fenêtre après validation
    }

    @FXML
    void onClose(ActionEvent event) {
        closeWindow(event);
    }

    public void setData(Purchase purchase) {
        this.purchase = purchase;
    
        if (purchase != null) {
            // Charger les MedicinePurchase associés à ce Purchase
            Set<MedicinePurchase> medicinePurchases = purchase.getMedicinesLink();
    
            chosenMedicinesBox.getChildren().clear(); // Nettoyer la liste des médicaments affichés
    
            for (MedicinePurchase medicinePurchase : medicinePurchases) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/cards/medicinePurchasedCard.fxml"));
                    Parent root = loader.load();
                    MedicinePurchasedCard controller = loader.getController();
    
                    controller.setData(medicinePurchase.getMedicine());
                    controller.setQuantitySelected(medicinePurchase.getQuantity());
                    root.setUserData(controller);
    
                    root.setOnMouseClicked(event -> styleBinderPurchased(root));
                    chosenMedicinesBox.getChildren().add(root);
                } catch (IOException e) {
                    e.printStackTrace(); // Gérer l'exception (log ou autre)
                }
            }
    
            // Mettre à jour le mode de paiement si nécessaire
            if (purchase.getPaymentMode() != null) {
                paymentBox.setValue(purchase.getPaymentMode());
            }
        }
    }

    public Purchase getPurchase() {
        return purchase;
    }

    private void closeWindow(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void addMedicineCard(Medicine medicine) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/cards/medicinePurchaseCard.fxml"));
        Parent root = loader.load();
        MedicinePurchaseCard loaderController = loader.getController();
        loaderController.setData(medicine);
        root.setUserData(loaderController);
        root.setOnMouseClicked(event -> styleBinderMedicines(root));
        medicinesBox.getChildren().add(root);
    }

    private void addMedicinePurchasedCard(Medicine medicine, int quantity) throws IOException {
        // Vérifier si le médicament est déjà choisi
        for (Node node : chosenMedicinesBox.getChildren()) {
            if (node.getUserData() instanceof MedicinePurchasedCard) {
                MedicinePurchasedCard existingCard = (MedicinePurchasedCard) node.getUserData();
                if (existingCard.getMedicine().getId() == medicineSelected.getId()) {
                    // Afficher un message si le médicament est déjà choisi
                    Dialogs.showSimpleMessage("Ce médicament a déjà été choisi");
                    return; // Sortir de la méthode pour éviter d'ajouter un doublon
                }
            }
        }
    
        // Charger la carte pour le médicament sélectionné
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/cards/medicinePurchasedCard.fxml"));
        Parent root = loader.load();
        MedicinePurchasedCard loaderController = loader.getController();
        loaderController.setData(medicine);
        loaderController.setQuantitySelected(quantity);
        root.setUserData(loaderController);
        root.setOnMouseClicked(event -> styleBinderPurchased(root));
        chosenMedicinesBox.getChildren().add(root);
    }
    

    private void styleBinderMedicines(Parent son) {
        // Récupérer le contrôleur associé à la carte actuelle
        MedicinePurchaseCard medicinePurchaseCard = (MedicinePurchaseCard) son.getUserData();
        
        // Si multipleSelector est sélectionné, appliquez le toggle sur la carte

            for (Node node : medicinesBox.getChildren()) {
                if (node.getUserData() != null && node != son) {
                    // Récupérer le contrôleur de chaque carte
                    MedicinePurchaseCard otherCardController = (MedicinePurchaseCard) node.getUserData();
                    // Appeler la méthode removeSelection() pour retirer le style "selected"
                    otherCardController.removeSelection();
                }
            }

            for (Node node : chosenMedicinesBox.getChildren()) {
                if (node.getUserData() != null && node != son) {
                    // Récupérer le contrôleur de chaque carte
                    MedicinePurchasedCard otherCardController = (MedicinePurchasedCard) node.getUserData();
                    // Appeler la méthode removeSelection() pour retirer le style "selected"
                    otherCardController.removeSelection();
                }
            }

            medicinePurchaseCard.toggle();

            if(medicinePurchaseCard.isCardSelected()) {
                medicineSelected = medicinePurchaseCard.getMedicine();
                medicineNameLabel.setText(medicineSelected.getName());
                medicineIV.setImage(imageHelper.loadImageFromResources(medicineSelected.getImage()));
            }
            else {
                medicineSelected = null;
                medicineIV.setImage(imageHelper.loadImageFromResources(null));
            }

    }

    private void styleBinderPurchased(Parent son) {
        // Récupérer le contrôleur associé à la carte actuelle
        MedicinePurchasedCard medicinePurchasedCard = (MedicinePurchasedCard) son.getUserData();
        
        for (Node node : chosenMedicinesBox.getChildren()) {
            if (node.getUserData() != null && node != son) {
                // Récupérer le contrôleur de chaque carte
                MedicinePurchasedCard otherCardController = (MedicinePurchasedCard) node.getUserData();
                // Appeler la méthode removeSelection() pour retirer le style "selected"
                otherCardController.removeSelection();
            }
        }

        for (Node node : medicinesBox.getChildren()) {
            if (node.getUserData() != null && node != son) {
                // Récupérer le contrôleur de chaque carte
                MedicinePurchaseCard otherCardController = (MedicinePurchaseCard) node.getUserData();
                // Appeler la méthode removeSelection() pour retirer le style "selected"
                otherCardController.removeSelection();
            }
        }

        medicinePurchasedCard.toggle();
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

    private void loadPaymentModes() {
        List<PaymentMode> paymentModes = paymentModeController.getAllPaymentModes();
        //paymentBox.getItems().addAll(paymentModes);

        paymentBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(PaymentMode paymentMode) {
                return paymentMode != null ? paymentMode.getName() : "";
            }
        
            @Override
            public PaymentMode fromString(String string) {
                return paymentBox.getItems().stream()
                        .filter(paymentMode -> paymentMode.getName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        if (paymentModes != null && !paymentModes.isEmpty()) {
            paymentBox.getItems().addAll(paymentModes);
            paymentBox.setValue(paymentModes.get(0));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadMedicines();
        loadPaymentModes();
        initializeSearcher();;
    }

    private boolean checkStock() {
        if(medicineSelected != null) {
            try {
                Integer.parseInt(medicineQuantityTF.getText().trim());
            }
            catch(Exception e) {
                Dialogs.showSimpleMessage("quantite invalide");
                return false;
            }
            if(medicineSelected.getStock() < Integer.parseInt(medicineQuantityTF.getText().trim())) {
                Dialogs.showSimpleMessage(String.format("quantite disponible de %s inssufisant", medicineSelected.getName()));
                return false;
            }          
            return true;
        }
        else {
            Dialogs.showSimpleMessage("veuillez selectionner un medicament");
            return false;
        }
    }

    private void validatePurchase() {
        if (chosenMedicinesBox.getChildren().isEmpty()) {
            Dialogs.showSimpleMessage("Veuillez sélectionner au moins un médicament avant de valider la commande.");
            return;
        }
    
        if (purchase == null) {
            // Création d'une nouvelle commande
            purchase = new Purchase();
            purchase.setUser(SessionUtil.getCurrentUser()); // Associer l'utilisateur connecté
            purchase.setPaymentMode((PaymentMode) paymentBox.getValue()); // Associer le mode de paiement sélectionné
            purchase.setClient(selectionedClient);
            purchase.calculateTotal();
            purchaseController.savePurchase(purchase); // Sauvegarder l'achat
        } else {
            // Mettre à jour le mode de paiement
            purchase.setPaymentMode((PaymentMode) paymentBox.getValue());
        }
    
        // Liste des médicaments à supprimer
        Set<MedicinePurchase> toRemove = new HashSet<>(purchase.getMedicinesLink());
    
        // Gérer les médicaments affichés dans l'interface
        for (Node node : chosenMedicinesBox.getChildren()) {
            MedicinePurchasedCard medicinePurchasedCard = (MedicinePurchasedCard) node.getUserData();
            Medicine medicine = medicinePurchasedCard.getMedicine();
            int quantity = medicinePurchasedCard.getQuantitySelected();
    
            // Vérifier si le médicament est déjà lié à l'achat
            MedicinePurchase existingMedicinePurchase = purchase.getMedicinePurchaseFor(medicine);
    
            if (existingMedicinePurchase != null) {
                // Met à jour la quantité si nécessaire
                if (existingMedicinePurchase.getQuantity() != quantity) {
                    existingMedicinePurchase.setQuantity(quantity);
                    medicinePurchaseController.updateMedicinePurchase(existingMedicinePurchase);
                }
                // Retirer des médicaments à supprimer
                toRemove.remove(existingMedicinePurchase);
            } else {
                // Ajouter un nouveau médicament
                MedicinePurchase newMedicinePurchase = new MedicinePurchase();
                newMedicinePurchase.setPurchase(purchase);
                newMedicinePurchase.setMedicine(medicine);
                newMedicinePurchase.setQuantity(quantity);
    
                purchase.addMedicinePurchase(newMedicinePurchase);
                medicinePurchaseController.saveMedicinePurchase(newMedicinePurchase);
            }
        }
    
        // Supprimer les médicaments qui ne sont plus affichés dans l'interface
        for (MedicinePurchase medicinePurchase : toRemove) {
            Medicine medicine = medicinePurchase.getMedicine();
    
            purchase.removeMedicinePurchase(medicinePurchase);
            medicine.removePurchaseLink(medicinePurchase);
    
            medicinePurchaseController.deleteMedicinePurchase(medicinePurchase.getId());
        }
    
        // Recalculer le total et mettre à jour la commande
        purchase.calculateTotal();

    
        purchaseController.updatePurchase(purchase);
        purchase = purchaseController.getPurchaseById(purchase.getId());
        Dialogs.showSimpleMessage("Commande validée avec succès !");
    }

    private void initializeSearcher() {
        searcher.textProperty().addListener((observable, oldValue, newValue) -> {
            searchMedicines(newValue.trim().toLowerCase());
        });
    }

    
    private void searchMedicines(String query) {
        // Effacer les résultats actuels
        medicinesBox.getChildren().clear();
    
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
    
}
