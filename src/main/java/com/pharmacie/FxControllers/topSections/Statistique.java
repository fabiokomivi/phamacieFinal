package com.pharmacie.FxControllers.topSections;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.pharmacie.FxControllers.cards.CriticalStock;
import com.pharmacie.controllers.CategoryController;
import com.pharmacie.controllers.MedicineController;
import com.pharmacie.controllers.PaymentModeController;
import com.pharmacie.controllers.PurchaseController;
import com.pharmacie.controllers.SupplierController;
import com.pharmacie.controllers.UserController;
import com.pharmacie.helper.SalesServices;
import com.pharmacie.models.Medicine;
import com.pharmacie.models.Purchase;
import com.pharmacie.utilities.Updatable;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Statistique implements Initializable , Updatable{

    MedicineController medicineController = new MedicineController();
    CategoryController categoryController = new CategoryController();
    UserController userController = new UserController();
    PaymentModeController paymentModeController = new PaymentModeController();
    SupplierController supplierController = new SupplierController();
    PurchaseController purchaseController = new PurchaseController();
    SalesServices salesServices = new SalesServices();

    @FXML
    private VBox criticStockBox;

    @FXML
    private AreaChart<String, Number> purchaseArea;

    @FXML
    private StackedBarChart<String, Number> salesBar;

    @FXML
    private Label categoryNumber;

    @FXML
    private Label medicineNumber;

    @FXML
    private Label paymentNumber;

    @FXML
    private Label supplierNumber;

    @FXML
    private Label userNumber;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addCriticalCard(Medicine medicine) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/pharmacie/fxml/cards/criticalStock.fxml"));
        Parent root = loader.load();
        CriticalStock controller = loader.getController();
        controller.setData(medicine);
        root.setUserData(controller);
        root.setOnMouseClicked(e -> styleBinder(root));
        criticStockBox.getChildren().add(root);

    }

    private void styleBinder(Parent card) {
        // Récupérer le contrôleur associé à la carte actuelle
        CriticalStock categoryCardController = (CriticalStock) card.getUserData();
        
        // Si multipleSelector n'est pas sélectionné, itérer sur les enfants du supplierGrid
        for (Node node : criticStockBox.getChildren()) {
            // Vérifier si l'élément a un contrôleur (on suppose que chaque node a un contrôleur associé)
            if (node.getUserData() != null && node != card) {
                // Récupérer le contrôleur de chaque carte
                CriticalStock otherCategoryCardController = (CriticalStock) node.getUserData();
                // Appeler la méthode removeSelection() pour retirer le style "selected"
                otherCategoryCardController.removeSelection();
            }
        }
        categoryCardController.toggle();
    }

    private void loadData() throws IOException {
        // Ajouter les cartes de stock critique
        for (Medicine medicine : medicineController.getAllMedicines()) {
            if (medicine.isCritic()) {
                addCriticalCard(medicine);
            }
        }
    
        // Effacer les données précédentes des graphiques
        salesBar.getData().clear();
        purchaseArea.getData().clear();
        criticStockBox.getChildren().clear();
    
        // Séries pour le graphique en barres (ventes)
        XYChart.Series<String, Number> purchaseTotalSeries = new XYChart.Series<>();
        purchaseTotalSeries.setName("Montant total des ventes");
    
        XYChart.Series<String, Number> purchaseSizeSeries = new XYChart.Series<>();
        purchaseSizeSeries.setName("Nombre de ventes");
    
        // Récupérer les ventes par jour de la semaine
        Map<String, List<Purchase>> weekSales = salesServices.getSalesByDayOfWeek(purchaseController.getAllPurchases());
    
        // Ajouter les données du dictionnaire aux séries
        for (Map.Entry<String, List<Purchase>> entry : weekSales.entrySet()) {
            String day = entry.getKey(); // Jour de la semaine (ex : "Lundi")
            List<Purchase> purchases = entry.getValue();
    
            // Ajouter les données à chaque série
            purchaseTotalSeries.getData().add(new XYChart.Data<>(day, salesServices.computeAmount(purchases)));
            purchaseSizeSeries.getData().add(new XYChart.Data<>(day, salesServices.countPurchases(purchases)));
        }
    
        // Ajouter les séries aux graphiques
        salesBar.getData().add(purchaseTotalSeries);
        purchaseArea.getData().add(purchaseSizeSeries);

        categoryNumber.setText(String.valueOf(categoryController.getAllCategories().size()));
        medicineNumber.setText(String.valueOf(medicineController.getAllMedicines().size()));
        supplierNumber.setText(String.valueOf(supplierController.getAllSuppliers().size()));
        paymentNumber.setText(String.valueOf(paymentModeController.getAllPaymentModes().size()));
        userNumber.setText(String.valueOf(userController.getAllUsers().size()));
    }

    @Override
    public void update() {
        try {
            loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

}
